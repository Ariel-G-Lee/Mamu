package com.example.mp_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

/**
 * MemoActivity
 * @author 김희주 이가빈
 */


public class MemoActivity extends AppCompatActivity {
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        //타이틀은 메모의 해당 날짜를 받아와서 설정 -> 추후 진행
        setTitle("날짜 받아와서 넣어야합니다");

        //뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editBtn = (Button) findViewById(R.id.btnEdit);
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
