package com.example.mp_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * MemoActivity
 * @author 조성주, 허윤서, 강주혜, 김희주, 이가빈
 */

public class MainActivity extends AppCompatActivity
        implements OnDateSelectedListener {
    ListView listView;
    ArrayList<ContentValues> DataList;
    MyAdapter myAdapter;
    static String date;
    DBHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        //CollapsingToolbar에 캘린더 표시하기 위해 연결
        CollapsingToolbarLayout mCollapseTooBar = (CollapsingToolbarLayout) findViewById(R.id.collapseToolbarLayout);

        setSupportActionBar(mToolbar);
        //타이틀 이름 삭제
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //배경 색상 흰색으로 설정
        mToolbar.setBackgroundColor(Color.WHITE);
        mCollapseTooBar.setBackgroundColor(Color.WHITE);


        handler = DBHandler.open(this);
        MaterialCalendarView widget = findViewById(R.id.calendarView);
//        MaterialCalendarView widgetWeek = findViewById(R.id.calendarViewWeek);

        Calendar calendar = Calendar.getInstance();

        widget.setDateSelected(calendar.getTime(), true);
        widget.setOnDateChangedListener(this);

//        widgetWeek.setDateSelected(calendar.getTime(), true);
//        widgetWeek.setOnDateChangedListener(this);

        date = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));

        //어댑터랑 리스트뷰xml이랑 연결
        this.InitializeData();
        listView = (ListView)findViewById(R.id.listView);
        myAdapter = new MyAdapter(this, DataList);
        listView.setAdapter(myAdapter);

        // 맨 아래 핑크색 동그라미 버튼 (플로팅버튼) 눌렀을때 Activity_edit 로 화면전환
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("key",-1); //key값 전달 -1이면 생성, 그밖은 수정.
                intent.putExtra("date",date);
                startActivity(intent);
                finish();
            }
        });

        //리스트 누르면 Activity Memo 로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                intent.putExtra("key",myAdapter.getItem(position).getAsInteger("Memo_ID")); //key값 전달
                intent.putExtra("date", date);
                startActivity(intent);
                finish();

            }
        });

        //material calendar month 옵션 설정
        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

//        widgetWeek.state().edit()
//                .setFirstDayOfWeek(Calendar.SUNDAY)
//                .setCalendarDisplayMode(CalendarMode.WEEKS)
//                .commit();
    }

    //데이터 초기화, 날짜에 맞는 데이터 불러와서 저장해주는곳
    private void InitializeData() {
        DataList = handler.select(date);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String year =Integer.toString(date.getYear());
        String month = Integer.toString(date.getMonth());
        String day = Integer.toString(date.getDay());

        MainActivity.date = year+month+day;

    }
}
