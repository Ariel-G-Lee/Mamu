package com.example.mp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * LoginActivity
 * @author 김희주
 */
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

        //view 연결
        TextId = (EditText)findViewById(R.id.inputid);
        TextPw = (EditText)findViewById(R.id.inputpw);
        loginbt = (Button)findViewById(R.id.login);
        signinbt = (Button)findViewById(R.id.signin);

        //비밀번호 EditText 엔터키 이벤트
        TextPw.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    LoginAction();
                    return true;
                }
                return false;
            }
        });

        //로그인 버튼 클릭
        loginbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LoginAction();
            }
        });

        //회원가입 버튼 클릭
        signinbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //로그인을 위해 아이디, 비밀번호 체크하는 메소드
    public void LoginAction(){
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
            Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userCode",UserInfo.getAsInteger("USER_CODE"));
            startActivity(intent);
            finish();
        }
    }
}
