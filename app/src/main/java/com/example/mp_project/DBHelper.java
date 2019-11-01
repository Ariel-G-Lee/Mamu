package com.example.mp_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        //Called when the database is created for the first time.
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
    public void onOpen(SQLiteDatabase db){
        //Called when the database has been opened.
        super.onOpen(db);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Called when the database needs to be upgraded.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
