package com.example.mp_project;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

/**
 * MemoActivity
 * @author 김희주 이가빈
 */


public class MemoActivity extends AppCompatActivity {
    DBHandler handler;
    Utils utils;

    Button editBtn;
    TextView titleView;
    TextView contentView;
    ImageView imgView;

    byte[] bytearrays;
    int key;
    String date;

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private YouTubePlayerSupportFragmentX youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;

    //youtube key
    String videoId;

    //API Key
    String apiKey = "AIzaSyDuy59FohjySY3Zq1LUDUiMG2yNmCNW5cY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //View 연결
        editBtn = (Button) findViewById(R.id.btnEdit);
        titleView = (TextView)findViewById(R.id.titleView);
        contentView = (TextView)findViewById(R.id.contentView);
        imgView = (ImageView)findViewById(R.id.imageView);

        //handler open
        handler = DBHandler.open(this);

        //utils
        utils = new Utils(getApplicationContext());

        //key 값 수신
        Intent intent = getIntent();
        key = intent.getExtras().getInt("key");
        date = intent.getExtras().getString("date");

        //memo 정보 불러오기
        ContentValues memo =  handler.selectOne(key);

        //set title
        setTitle(dateformat(date));

        youTubePlayerFragment = (YouTubePlayerSupportFragmentX) getSupportFragmentManager()
                .findFragmentById(R.id.youtubeFragment);

        //set data
        if(key>0){
            titleView.setText(memo.getAsString("MemoTitle"));
            contentView.setText(memo.getAsString("MemoContents"));
            bytearrays = memo.getAsByteArray("Image");
            imgView.setImageBitmap(utils.ByteArraytoBitmap(bytearrays));
            String fullUrl = memo.getAsString("YoutubeUrl");
            // url이 DB에 입력되지 않았을 경우 프래그먼트 hide
            if(fullUrl.equals("")){
                fragmentTransaction.hide(youTubePlayerFragment);
                fragmentTransaction.commit();
            } else {
                //전체 url에서 youtube key 추출
                String[] array = fullUrl.split("/");
                videoId = array[array.length-1];
                //set youtube player
                initializeYoutubePlayer(youTubePlayerFragment);
            }
        }
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
                switch(item.getTitle().toString()){
                    case "수정":
                        Intent intent = new Intent(MemoActivity.this, EditActivity.class);
                        intent.putExtra("key", key); //key값 전달
                        intent.putExtra("date", date);
                        startActivity(intent);
                        finish();
                        break;
                    case "삭제":
                        //지우고 메인 화면으로 돌아감
                        handler.delete(key);
                        startActivity(new Intent(MemoActivity.this,MainActivity.class));
                        finish();
                        break;
                    case "취소":
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    //유튜브 플레이어 메소드
    private void initializeYoutubePlayer(YouTubePlayerSupportFragmentX youTubePlayerFragment) {
//        youTubePlayerFragment = (YouTubePlayerSupportFragmentX) getSupportFragmentManager()
//                .findFragmentById(R.id.youtubeFragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(apiKey, new YouTubePlayer.OnInitializedListener() {
            //초기화 성공
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo(videoId);
                    youTubePlayer.play();
                }
            }
            //초기화 실패
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            }
        });
        fragmentTransaction.show(youTubePlayerFragment);
    }
    //뒤로가기 버튼 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                startActivity(new Intent(MemoActivity.this,MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //물리적 뒤로가기 버튼 메소드
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MemoActivity.this,MainActivity.class));
        finish();
    }

    public String dateformat(String date){
        return date.substring(0,4)+"년 "+date.substring(4,6)+"월 "+date.substring(6)+"일";
    }
}
