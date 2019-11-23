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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    Utils utils;
    Button saveBtn;
    Button imgBtn;
    EditText editTitle;
    EditText editURL;
    EditText editMemo;
    EditText editTag;
    ImageView imgView;
    RadioGroup rg;
    RadioButton feelBtn;
    byte[] bytearrays = {};

    int key;
    String date;
    int memCode;
    String feel="";

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

        //라디오그룹 이용 기분 이모티콘 설정
        rg = (RadioGroup)findViewById(R.id.feelingGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.f1 : feel = "f1"+Integer.toString(checkedId);
                        break;
                    case R.id.f2 : feel = "f2"+Integer.toString(checkedId);
                        break;
                    case R.id.f3 : feel = "f3"+Integer.toString(checkedId);
                        break;
                    case R.id.f4 : feel = "f4"+Integer.toString(checkedId);
                        break;
                    case R.id.f5 : feel = "f5"+Integer.toString(checkedId);
                        break;
                }
            }
        });

        //View 연결
        editTitle = (EditText)findViewById(R.id.editTitle);
        editURL = (EditText)findViewById(R.id.editURL);
        editMemo = (EditText)findViewById(R.id.editMemo);
        editTag = (EditText)findViewById(R.id.tag);
        imgBtn = (Button)findViewById(R.id.btnAdd);
        saveBtn = (Button)findViewById(R.id.btnSave);
        imgView = (ImageView)findViewById(R.id.imageView);


        //handler open
        handler = DBHandler.open(this);

        //utils
        utils = new Utils(getApplicationContext());

        //key 값 수신
        Intent intent = getIntent();
        key = intent.getExtras().getInt("key");
        date = intent.getExtras().getString("date");
        memCode = intent.getExtras().getInt("userCode");

        //memo 정보 불러오기
        ContentValues memo =  handler.selectOne(key,memCode);

        //set title
        setTitle(dateformat(date));

        //set data
        if(key>0){
            editTitle.setText(memo.getAsString("MemoTitle"));
            editURL.setText(memo.getAsString("YoutubeUrl"));
            editMemo.setText(memo.getAsString("MemoContents"));
            editTag.setText(memo.getAsString("MemoTag"));
            bytearrays = memo.getAsByteArray("Image");
            imgView.setImageBitmap(utils.ByteArraytoBitmap(bytearrays));
            if(!memo.getAsString("MemoFeel").equals("")){
                int id = Integer.parseInt(memo.getAsString("MemoFeel").substring(2));
                feelBtn = (RadioButton)findViewById(id);
                feelBtn.setChecked(true);
            }
        }

        //저장버튼
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ContentValues values = new ContentValues();
                values.put("CreationDate", date);
                values.put("MemoTitle",editTitle.getText().toString());
                values.put("MemoContents",editMemo.getText().toString());
                values.put("MemoTag", editTag.getText().toString());
                values.put("MemoFeel", feel);
                values.put("YoutubeUrl",editURL.getText().toString());
                values.put("Image", bytearrays); // 사진 : bytearrays
                values.put("userCode",memCode);

                if(key<0){ // 생성-저장
                    key = (int)handler.insert(values);
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }else{ // 수정
                    handler.update(key,values);
                    Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                }
                //MemoActivity로 이동
                Intent i = new Intent(EditActivity.this, MemoActivity.class);
                i.putExtra("key", key); //key값 전달
                i.putExtra("date", date);
                i.putExtra("userCode",memCode);
                startActivity(i);
                //현재 액티비티 종료하고 넘어가기
                finish();
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
                bitmap = utils.resize(data.getData(),600);
                inputStream.close();

                imgView.setImageBitmap(bitmap);
                bytearrays= utils.BitmapToByteArray(bitmap);

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
                Toast.makeText(getApplicationContext(), "권한설정이 필요합니다.", Toast.LENGTH_SHORT).show();
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

    //뒤로가기 버튼 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작(edit화면에서 뒤로가기 누르면 메인으로 가야함)
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("key",key); //key값 전달
                intent.putExtra("date", date);
                intent.putExtra("userCode",memCode);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //물리적 뒤로가기 버튼 메소드
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        intent.putExtra("key",key); //key값 전달
        intent.putExtra("date", date);
        intent.putExtra("userCode",memCode);
        startActivity(intent);
        finish();
    }

    public String dateformat(String date){
        return date.substring(0,4)+"년 "+date.substring(4,6)+"월 "+date.substring(6)+"일";
    }
}
