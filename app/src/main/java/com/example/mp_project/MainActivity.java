package com.example.mp_project;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
    int memCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarMain);

        handler = DBHandler.open(this);

        //------------이가빈------------
        //CollapsingToolbar에 캘린더 표시하기 위해 연결
        CollapsingToolbarLayout mCollapseTooBar = (CollapsingToolbarLayout) findViewById(R.id.collapseToolbarLayout);

        setSupportActionBar(mToolbar);
        //타이틀 이름 삭제
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //-------------------------------

        //------------김희주------------
        Intent intent = getIntent();
        memCode = intent.getExtras().getInt("userCode");
        //-------------------------------

        //------------조성주------------
        MaterialCalendarView widget = findViewById(R.id.calendarView);
//        MaterialCalendarView widgetWeek = findViewById(R.id.calendarViewWeek);

        Calendar calendar = Calendar.getInstance();

        widget.setDateSelected(calendar.getTime(), true);
        widget.setOnDateChangedListener(this);
        //-------------------------------

        date = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));

        //------------허윤서------------
        //어댑터랑 리스트뷰xml이랑 연결
        listView = (ListView)findViewById(R.id.listView);
        DataList = handler.select(date, memCode);
        myAdapter = new MyAdapter(this, DataList);
        listView.setAdapter(myAdapter);
        //-------------------------------

        //------------강주혜------------
        // 맨 아래 핑크색 동그라미 버튼 (플로팅버튼) 눌렀을때 Activity_edit 로 화면전환
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("key",-1); //key값 전달 -1이면 생성, 그밖은 수정.
                intent.putExtra("date",date);
                intent.putExtra("userCode",memCode);
                startActivity(intent);
                finish();
            }
        });
        //-------------------------------

        //------------김희주------------
        //리스트 누르면 Activity Memo 로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                intent.putExtra("key",myAdapter.getItem(position).getAsInteger("Memo_ID")); //key값 전달
                intent.putExtra("date", date);
                intent.putExtra("userCode",memCode);
                startActivity(intent);
                finish();

            }
        });
        //-------------------------------

        //------------조성주------------
        //material calendar month 옵션 설정
        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        //-------------------------------
    }

    //리스트 초기화 - 김희주
    private void InitializeList() {
        DataList = handler.select(date,memCode);
        myAdapter.setItem(DataList);
        myAdapter.notifyDataSetChanged();
    }

    //------------조성주------------
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String year =Integer.toString(date.getYear());
        String month = Integer.toString(date.getMonth()+1);
        String day = Integer.toString(date.getDay());

        MainActivity.date = year+month+day;
        InitializeList();
    }
    //-------------------------------
}
