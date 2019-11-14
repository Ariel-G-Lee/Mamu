package com.example.mp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    DBHandler handler;
    Button loginbt;
    Button signinbt;

    EditText TextId;
    EditText TextPw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = DBHandler.open(this);

        TextId = (EditText)findViewById(R.id.inputid);
        TextPw = (EditText)findViewById(R.id.inputpw);

        loginbt = (Button)findViewById(R.id.login);
        signinbt = (Button)findViewById(R.id.signin);

        loginbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text_id = TextId.getText().toString();
                String text_pw = TextPw.getText().toString();

                ContentValues UserInfo = handler.selectMember(text_id);

                if(UserInfo.size() <= 0){
                    Toast.makeText(getApplicationContext(), "해당 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!(UserInfo.getAsString("USER_PW").equals(text_pw))){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else{//로그인 성공 - 메인화면 이동
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        //회원가입으로 이동
        signinbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
