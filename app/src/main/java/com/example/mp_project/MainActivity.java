package com.example.mp_project;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

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
            public void onClick(View v) {

            }
            /*
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MainActivity.this, DetailActivity.class));
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("key",1); //key값 전달 -1이면 생성, 그밖은 수정.
                intent.putExtra("date","191103");
                startActivity(intent);
            }
            */
        });

        MaterialCalendarView widget = findViewById(R.id.calendarView);

        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 12, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }


}
