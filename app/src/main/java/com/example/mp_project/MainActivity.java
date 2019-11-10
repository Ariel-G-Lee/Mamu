package com.example.mp_project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * MemoActivity
 * @author 조성주
 * @author 허윤서,강주혜
 */

public class MainActivity extends AppCompatActivity {
    private ListView list;
    ArrayList<SampleData> movieDataList;
    int date;
    DBHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //어댑터랑 리스트뷰xml이랑 연결
        this.InitializeMovieData();
        ListView listView = (ListView)findViewById(R.id.listView);
        final MyAdapter myAdapter = new MyAdapter(this,movieDataList);
        listView.setAdapter(myAdapter);


        //클릭해서 스와이프한후 날짜를 받아오면 date를 선택된날짜로 지정해주는부분. 일단 임의로 날짜를정해둠
        date=191109;


        // 맨 아래 핑크색 동그라미 버튼 (플로팅버튼) 눌렀을때 Activity_edit 로 화면전환
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
        //리스트 나열된거 누르면 Activity Memo 로 이동......일단 토스처리해둠
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getname(),
                        Toast.LENGTH_LONG).show();
            }
        });



        MaterialCalendarView widget = findViewById(R.id.calendarView);
        //material calendar 옵션 설정
        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 12, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }


    //데이터 초기화, 날짜에 맞는 데이터 불러와서 저장해주는곳
    private void InitializeMovieData() {
        handler = DBHandler.open(this);


        movieDataList = new ArrayList<SampleData>();
        /*
        movieDataList.add(new SampleData(R.drawable.movieposter1, "미션 이상임파서블","15세관람가"));
        movieDataList.add(new SampleData(R.drawable.movieposter2, "아저씨","19세 이상관람가"));
        movieDataList.add(new SampleData(R.drawable.movieposter3, "어벤져스","12세 이상관람가"));
         */
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from UserMemo where _CreationDate="+date,null);
        String photo=null;
        while (cursor.moveToNext()){
            movieDataList.add(new SampleData(R.drawable.ic_add_black_24dp, cursor.getString(3),cursor.getString(2)));
            // @@@@@@@@@@@R.Drawable 다음에 아이콘그림은 바꿔서 추가해주기 (icon)
            // 한마디로 Date 값에 해당하는 데이터들을 무비데이터 List에 연속해서 집어넣어주는데
            //getString 3번쨰열을 이름(name)에  2번째 열을 내용(content)에다 집어넣어준다
            //이건 데이터에 이름이랑 내용이 세번쨰랑 두번쨰에 있어서
        }
    }
}
