package com.example.mp_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * SigninActivity
 * @author 김희주
 */
public class SigninActivity extends AppCompatActivity {
    DBHandler handler;
    InputMethodManager imm;

    Button checkid;
    Button signinbt;

    EditText TextId;
    EditText TextPw ;
    EditText TextPwSame;
    EditText TextName;
    EditText TextPhone;
    EditText TextAddr;
    EditText TextBirth;

    RadioGroup RadioG;

    TextView CheckIdText;
    TextView CheckPwText;
    TextView SamePwText;

    Calendar calendar;
    int birthyear;
    int birthmonth;
    int birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        handler = DBHandler.open(this);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //view 연결
        TextId = (EditText)findViewById(R.id.sign_id);
        TextPw = (EditText)findViewById(R.id.sign_pw);
        TextPwSame = (EditText)findViewById(R.id.sign_pw_check);
        TextName = (EditText)findViewById(R.id.sign_name);
        TextPhone = (EditText)findViewById(R.id.sign_phone);
        TextAddr = (EditText)findViewById(R.id.sign_addr);
        TextBirth = (EditText)findViewById(R.id.birthday);

        RadioG = (RadioGroup)findViewById(R.id.radioG);

        CheckIdText = (TextView)findViewById(R.id.id_text);
        CheckPwText = (TextView)findViewById(R.id.pw_text);
        SamePwText = (TextView)findViewById(R.id.pw_check_text);

        checkid = (Button)findViewById(R.id.checkId);
        signinbt = (Button)findViewById(R.id.signin);


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


        //TextBirth 포커스 리스너 - 키보드 숨기기
        TextBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    imm.hideSoftInputFromWindow(TextBirth.getWindowToken(),0);
                    BirthdayPicker();
                }
            }
        });

        //TextBirth 클릭 리스너 - 키보드 숨기기
        TextBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(TextBirth.getWindowToken(),0);
                BirthdayPicker();
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
                String text_birth = TextBirth.getText().toString();

                //개인정보 동의 확인
                if(RadioG.getCheckedRadioButtonId() != R.id.radioY){
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
                UserInfo.put("USER_BIRTH",text_birth);

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


    //생년월일 picker
    public void BirthdayPicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.content_datepicker, null);

        //title 설정
        builder.setTitle("생년월일");

        NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        final NumberPicker datePicker = (NumberPicker) dialog.findViewById(R.id.picker_date);

        calendar = Calendar.getInstance();
        birthyear = 2000;
        birthmonth = 0;
        birthdate = 1;

        //연,월,일 min max value 설정, 2000.1.1로 기본 선택
        yearPicker.setMinValue(1950);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));
        yearPicker.setValue(2000);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(birthmonth+1);

        datePicker.setMinValue(1);
        datePicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        datePicker.setValue(birthdate);

        //연도 변경 리스너 - 윤년/평년 구분 위함.
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                birthyear = newVal;
                calendar.set(birthyear,birthmonth,birthdate);
                datePicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        });

        //월 변경 리스너 - 달마다 달라지는 최대 날짜 설정.
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                birthmonth = newVal - 1;
                calendar.set(birthyear,birthmonth,birthdate);
                datePicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        });

        //확인 버튼 클릭
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String birth = birthyear + "." + (birthmonth+1) + "." + birthdate;
                        TextBirth.setText(birth);
                    }
                });

        //취소 버튼 클릭
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setView(dialog);
        builder.show();
    }
}
