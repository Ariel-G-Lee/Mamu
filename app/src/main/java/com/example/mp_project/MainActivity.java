package com.example.mp_project;

import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 맨 아래 핑크색 동그라미 버튼 (플로팅버튼) 눌렀을때 DetailActivity.class로 화면전환
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MainActivity.this, DetailActivity.class));
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("key",1); //key값 전달 -1이면 생성, 그밖은 수정.
                intent.putExtra("date","191103");
                startActivity(intent);
            }
        });
    }


}
