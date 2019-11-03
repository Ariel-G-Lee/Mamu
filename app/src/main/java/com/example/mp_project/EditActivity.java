package com.example.mp_project;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Map;

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
        saveBtn = (Button)findViewById(R.id.btnEdit);

        //handler open
        handler = DBHandler.open(this);

        //key 값 수신
        Intent intent = getIntent();
        final int key = intent.getExtras().getInt("key");
        final String date = intent.getExtras().getString("date");

        //memo 정보 불러오기
        Map<String,String> memo =  handler.selectOne(key);

        //set title
        setTitle(date);

        //set data
        if(key>0){
            editTitle.setText(memo.get("MemoTitle"));
            editURL.setText(memo.get("YoutubeUrl"));
            editMemo.setText(memo.get("MemoContents"));
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
               // values.put("ImagePath",""); // 사진 url

                if(key<0){ // 생성-저장
                    handler.insert(values);
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }else{ // 수정
                    handler.update(key,values);
                    Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                }

                //메인화면으로 돌아가기
                startActivity(new Intent(EditActivity.this,MainActivity.class));
            }
        });

        //사진첨부하기 + 버튼
        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "사진첨부하기", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
