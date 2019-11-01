package com.example.mp_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHandler {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String TableName;

    private DBHandler(Context context) throws SQLiteException {
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
        this.TableName = dbHelper.getTableName();
    }

    public static DBHandler open(Context context) throws SQLiteException{
        DBHandler dbHandler = new DBHandler(context);
        return dbHandler;
    }

    public void close(){
        dbHelper.close();
    }

    public int insert(ContentValues values){
        //new memo insert
        values.put(dbHelper.COLUMN_USEYN,"Y");
        if(db.insertOrThrow(TableName,null,values)<0){
            //실패
            return -1;
        }
        return 0;
    }

    public int delete(int key){
        String where = dbHelper.COLUMN_ID + " = " + key;
        //db.delete(dbHelper.getTableName(),where, null); //완전삭제

        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_USEYN,"N");

        return db.update(TableName, values, where, null); //간접삭제
    }

    public int update(int key, ContentValues values){
        String where = dbHelper.COLUMN_ID + " = " + key;
        return db.update(TableName, values, where, null);
    }

    public ArrayList<Map<String,String>> select(String date){
        //해당 날짜의 메모 전부 select
        String selection = dbHelper.COLUMN_DATE+"=? AND "+dbHelper.COLUMN_USEYN +"=?";
        Cursor cursor = db.query(TableName, null, selection, new String[]{date,"Y"}, null, null, null);

        if(!cursor.moveToFirst()){
            //해당 날짜 메모 없음
            return null;
        }

        ArrayList<Map<String,String>> list= new ArrayList<Map<String,String>>();
        while(true){
            Map<String,String> tmp = new HashMap<String,String>();

            tmp.put("Memo_ID",Integer.toString(cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_ID))));
            tmp.put("CreationDate",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DATE)));
            tmp.put("MemoContents",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_CONTENTS)));
            tmp.put("MemoTitle",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_TITLE)));
            tmp.put("YoutubeUrl",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_YTBURL)));
            tmp.put("ImagePath",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_IMGPATH)));
            tmp.put("UseYN",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_USEYN)));

            list.add(tmp);

            if(!cursor.moveToNext()) break;
        }

        return list;

    }

}
