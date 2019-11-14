package com.example.mp_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * MemoActivity
 * @author 조성주, 허윤서, 강주혜, 김희주
 */

public class MainActivity extends AppCompatActivity
        implements OnDateSelectedListener {
    ListView listView;
    ArrayList<ContentValues> DataList;
    MyAdapter myAdapter;
    String date;
    DBHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = DBHandler.open(this);

        MaterialCalendarView widget = findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);

        //클릭해서 스와이프한후 날짜를 받아오면 date를 선택된날짜로 지정해주는부분. 일단 임의로 날짜를정해둠
        date="191103";

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

        //리스트 나열된거 누르면 Activity Memo 로 이동......일단 토스처리해둠
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                intent.putExtra("key",myAdapter.getItem(position).getAsInteger("Memo_ID")); //key값 전달
                intent.putExtra("date", date);
                startActivity(intent);
                finish();

                /*Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getAsString("MemoTitle"),
                        Toast.LENGTH_LONG).show();*/
            }
        });

        //material calendar 옵션 설정
        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 12, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
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

        String text = year+month+day;
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();


    }
}
