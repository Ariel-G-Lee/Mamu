package com.example.mp_project;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * EditActivity
 * @author 김희주 이가빈
 */

public class EditActivity extends AppCompatActivity {

    Button editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //타이틀은 메모의 해당 날짜를 받아와서 설정 -> 추후 진행
        setTitle("날짜 받아와서 넣어야합니다");

        //뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
