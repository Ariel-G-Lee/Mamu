package com.example.mp_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB 관리 클래스
 * @author 김희주
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mamu.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "UserMemo";
    static final String COLUMN_ID = "Memo_ID";
    static final String COLUMN_DATE = "CreationDate";
    static final String COLUMN_CONTENTS = "MemoContents";
    static final String COLUMN_TITLE = "MemoTitle";
    static final String COLUMN_YTBURL = "YoutubeUrl";
    static final String COLUMN_IMGPATH = "ImagePath";
    static final String COLUMN_USEYN = "UseYN";

    //Constructor
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    //DB가 처음으로 만들어질 때 불리는 메소드.
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME +" ("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE +" TEXT NOT NULL,"
                + COLUMN_CONTENTS + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_YTBURL + " TEXT,"
                + COLUMN_IMGPATH + " TEXT,"
                + COLUMN_USEYN+ " TEXT"
                + ");";

        db.execSQL(sql);

    }

    //DB가 이미 오픈되어 있을 때 불리는 메소드
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);

    }

    //DB 업그레이드 메소드. 테이블을 삭제하고 새로 만들어서 데이터 전부 삭제
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
