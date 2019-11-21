package com.example.mp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * SigninActivity
 * @author 김희주
 */
public class SigninActivity extends AppCompatActivity {
    DBHandler handler;
    Button checkid;
    Button signinbt;

    EditText TextId;
    EditText TextPw ;
    EditText TextPwSame;
    EditText TextName;
    EditText TextPhone;
    EditText TextAddr;
    RadioGroup RadioG;
    TextView CheckIdText;
    TextView CheckPwText;
    TextView SamePwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        handler = DBHandler.open(this);

        //view 연결
        TextId = (EditText)findViewById(R.id.sign_id);
        TextPw = (EditText)findViewById(R.id.sign_pw);
        TextPwSame = (EditText)findViewById(R.id.sign_pw_check);
        TextName = (EditText)findViewById(R.id.sign_name);
        TextPhone = (EditText)findViewById(R.id.sign_phone);
        TextAddr = (EditText)findViewById(R.id.sign_addr);

        RadioG = (RadioGroup)findViewById(R.id.radioG);
        CheckIdText = (TextView)findViewById(R.id.id_text);
        CheckPwText = (TextView)findViewById(R.id.pw_text);
        SamePwText = (TextView)findViewById(R.id.pw_check_text);

        checkid = (Button)findViewById(R.id.checkId);
        signinbt = (Button)findViewById(R.id.signin);

        //비밀번호 입력창 변경 시 호출되는 메소드
        TextPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = TextPw.getText().toString();
                if(pw.length() < 8 || pw.length() > 16 || !pw.matches("[a-zA-Z0-9]+") || !pw.matches("^.*[0-9].*$") || !pw.matches("^.*[a-zA-Z].*$")){
                    CheckPwText.setText("비밀번호는 8자에서 16자, 영어와 숫자를 꼭 포함해야 합니다.");
                }
                else {
                    CheckPwText.setText("올바른 비밀번호 유형입니다.");
                }
                SamePwText.setText("");

            }
        });

        //비밀번호 입력확인창 변경 시 호출되는 메소드
        TextPwSame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextPw.getText().toString().equals(TextPwSame.getText().toString())){
                    SamePwText.setText("비밀번호가 일치합니다.");
                }
                else{
                    SamePwText.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

        //아이디 입력창 변경 시 호출되는 메소드
        TextId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckIdText.setText("");
            }
        });

        //아이디 중복확인 버튼 클릭 메소드
        checkid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text_id =  TextId.getText().toString();
                ContentValues UserInfo = handler.selectMember(text_id);

                if(UserInfo.size() <= 0){ // 중복이 아니라면
                    CheckIdText.setText("아이디를 사용할 수 있습니다.");
                }
                else{
                    CheckIdText.setText("중복 아이디입니다.");
                }
            }
        });

        //회원가입 버튼 클릭 메소드
        signinbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text_id = TextId.getText().toString();
                String text_pw = TextPw.getText().toString();
                String text_name = TextName.getText().toString();
                String text_phone = TextPhone.getText().toString();
                String text_addr = TextAddr.getText().toString();


                if(RadioG.getCheckedRadioButtonId() != R.id.radioY){//개인정보 동의 확인
                    Toast.makeText(getApplicationContext(), "개인정보 동의를 하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //아이디 체크
                if(CheckIdText.getText().equals("") || CheckIdText.getText().equals("중복 아이디입니다.")){
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                // 비밀번호 체크
                if(CheckPwText.getText().equals("비밀번호는 8자에서 16자, 영어와 숫자를 꼭 포함해야 합니다.") || CheckPwText.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                //비밀번호 확인 체크
                if(SamePwText.getText().equals("비밀번호가 일치하지 않습니다.")||SamePwText.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 다시 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                //유저 정보 values에 저장
                ContentValues UserInfo = new ContentValues();
                UserInfo.put("USER_ID",text_id);
                UserInfo.put("USER_PW",text_pw);
                UserInfo.put("USER_NAME",text_name);
                UserInfo.put("USER_PHONE",text_phone);
                UserInfo.put("USER_ADDRESS",text_addr);

                //유저 정보 db에 저장
                handler.UserSignin(UserInfo);

                Toast.makeText(getApplicationContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                //첫번째 화면으로 이동
                startActivity(new Intent(SigninActivity.this, LoginActivity.class));
                finish();


            }
        });
    }


    //물리적 뒤로가기 버튼 메소드
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SigninActivity.this,LoginActivity.class));
        finish();
    }
}
