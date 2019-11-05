package com.example.mp_project;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * EditActivity
 * @author 김희주 이가빈
 */

public class EditActivity extends AppCompatActivity {
    DBHandler handler;
    Button saveBtn;
    Button imgBtn;
    EditText editTitle;
    EditText editURL;
    EditText editMemo;
    ImageView imgView;
    byte[] bytearrays;

    private static final int PICK_IMAGE = 1;
    private static final int GALLERY_PERMISSION = 2;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //View 연결
        editTitle = (EditText)findViewById(R.id.editTitle);
        editURL = (EditText)findViewById(R.id.editURL);
        editMemo = (EditText)findViewById(R.id.editMemo);
        imgBtn = (Button)findViewById(R.id.btnAdd);
        saveBtn = (Button)findViewById(R.id.btnSave);
        imgView = (ImageView)findViewById(R.id.imageView);

        //handler open
        handler = DBHandler.open(this);

        //key 값 수신
        Intent intent = getIntent();
        final int key = intent.getExtras().getInt("key");
        final String date = intent.getExtras().getString("date");

        //memo 정보 불러오기
        ContentValues memo =  handler.selectOne(key);

        //set title
        setTitle(date);

        //set data
        if(key>0){
            editTitle.setText(memo.getAsString("MemoTitle"));
            editURL.setText(memo.getAsString("YoutubeUrl"));
            editMemo.setText(memo.getAsString("MemoContents"));
            bytearrays = memo.getAsByteArray("Image");
            imgView.setImageBitmap(ByteArraytoBitmap(bytearrays));
        }

        //저장버튼
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ContentValues values = new ContentValues();
                values.put("CreationDate", date);
                values.put("MemoContents",editMemo.getText().toString());
                values.put("MemoTitle",editTitle.getText().toString());
                values.put("YoutubeUrl",editURL.getText().toString());
                values.put("Image", bytearrays); // 사진 : bytearrays

                if(key<0){ // 생성-저장
                    long tmp = handler.insert(values);
                    Toast.makeText(getApplicationContext(), "저장되었습니다."+tmp, Toast.LENGTH_SHORT).show();
                }else{ // 수정
                    long tmp = handler.update(key,values);
                    Toast.makeText(getApplicationContext(), "수정되었습니다."+tmp, Toast.LENGTH_SHORT).show();
                }

                //메인화면으로 돌아가기
                startActivity(new Intent(EditActivity.this,MainActivity.class));
            }
        });

        //사진첨부하기 '+' 버튼
        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //권한설정 체크
                if(ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    //권한설정이 안 된 경우
                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},GALLERY_PERMISSION);
                }
                else{
                    //권한설정이 돼있는 경우
                    pickImg();
                }
            }
        });


    }

    //호출되었다가 돌아올 때 결과 받는 메소드
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = resize(data.getData(),600);
                inputStream.close();

                imgView.setImageBitmap(bitmap);
                bytearrays= BitmapToByteArray(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Permission 요청되었을 때 호출되는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        if(requestCode==GALLERY_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //권한 설정 승인했을 때
                pickImg();
            }else{
                //권한 설정 승인하지 않았을 때
                Toast.makeText(getApplicationContext(), "권한설정이 안됐습니다~~", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //이미지를 선택하는 intent를 불러오고, resultCode(PICK_IMAGE) 전달
    private void pickImg(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooseIntent = Intent.createChooser(getIntent,"Select image");
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pickIntent});
        startActivityForResult(chooseIntent,PICK_IMAGE);
    }

    //Bitmap을 ByteArray로 변경(DB에 저장할 때 사용)
    private byte[] BitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }

    //ByteArrray를 Bitmap으로 변경(이미지뷰에 사용)
    private Bitmap ByteArraytoBitmap(byte[] bytearray){
        return BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
    }

    //Bitmap 리사이징 메소드
    private Bitmap resize(Uri uri, int size){
        Bitmap resizeBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;  //메모리할당 없이 width, height, mimetype 셋팅
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while(true){
                if((width/2 < size) && (height/2 < size)){
                    break;
                }
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            options.inJustDecodeBounds = false;
            resizeBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return resizeBitmap;
    }
}
