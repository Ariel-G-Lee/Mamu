package com.example.mp_project;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

/**
 * MemoActivity
 * @author 김희주 이가빈
 */


public class MemoActivity extends AppCompatActivity {
    DBHandler handler;
    Utils utils;

    Button editBtn;
    TextView titleView;
    TextView contentView;
    ImageView imgView;

    byte[] bytearrays;
    int key;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //View 연결
        editBtn = (Button) findViewById(R.id.btnEdit);
        titleView = (TextView)findViewById(R.id.titleView);
        contentView = (TextView)findViewById(R.id.contentView);
        imgView = (ImageView)findViewById(R.id.imageView);

        //handler open
        handler = DBHandler.open(this);

        //utils
        utils = new Utils(getApplicationContext());

        //key 값 수신
        Intent intent = getIntent();
        key = intent.getExtras().getInt("key");
        date = intent.getExtras().getString("date");

        //memo 정보 불러오기
        ContentValues memo =  handler.selectOne(key);

        //set title
        setTitle(date);

        //set data
        if(key>0){
            titleView.setText(memo.getAsString("MemoTitle"));
            contentView.setText(memo.getAsString("MemoContents"));
            bytearrays = memo.getAsByteArray("Image");
            imgView.setImageBitmap(utils.ByteArraytoBitmap(bytearrays));
        }
    }

    //팝업 메뉴 메소드
    public void mOnClick(View v){
        //팝업메뉴 객체 생성
        PopupMenu popup = new PopupMenu(MemoActivity.this, editBtn);
        //팝업메뉴 xml 파일 inflate
        popup.getMenuInflater().inflate(R.menu.edit_menu, popup.getMenu());

        //팝업 메뉴 클릭시 이벤트
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getTitle().toString()){
                    case "수정":
                        Intent intent = new Intent(MemoActivity.this, EditActivity.class);
                        intent.putExtra("key", key); //key값 전달
                        intent.putExtra("date", date);
                        startActivity(intent);
                        break;
                    case "삭제":
                        //지우고 메인 화면으로 돌아감
                        handler.delete(key);
                        startActivity(new Intent(MemoActivity.this,MainActivity.class));
                        break;
                    case "취소":
                        break;
                }

                //임시 테스트용
                Toast.makeText(
                        MemoActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });
        popup.show();
    }

}
