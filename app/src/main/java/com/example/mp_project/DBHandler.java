package com.example.mp_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;


/**
 * DB 데이터 관리 클래스
 * @author 김희주
 */
public class DBHandler {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String TableName;

    //Constructor
    private DBHandler(Context context) throws SQLiteException {
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
        this.TableName = dbHelper.getTableName();
    }

    //constructor 대신 이 메소드를 불러서 DBHandler을 사용함
    public static DBHandler open(Context context) throws SQLiteException{
        DBHandler dbHandler = new DBHandler(context);
        return dbHandler;
    }


    //DB를 직접 닫지 않고 Helper 클래스만 close 함.
    public void close(){
        dbHelper.close();
    }

    /**
     * DB에 새로운 데이터를 insert함.
     * 이 때, values에는 UseYN 정보와 PK가 포함될 필요 없고 내부적으로 처리된다.
     * @param values (ContentValues - 여기서 키값은 어트리뷰트명과 동일)
     * @return 성공: 삽입된 열의 번호(long), 실패: -1
     */
    public long insert(ContentValues values){
        values.put(dbHelper.COLUMN_USEYN,"Y");

        String sql = "INSERT INTO " + TableName+" ("
//                + dbHelper.COLUMN_ID +","
                + dbHelper.COLUMN_DATE +","
                + dbHelper.COLUMN_CONTENTS + ","
                + dbHelper.COLUMN_TITLE + ","
                + dbHelper.COLUMN_YTBURL + ","
                + dbHelper.COLUMN_IMG + ","
                + dbHelper.COLUMN_USEYN+ ") VALUES(?,?,?,?,?,?)";

        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,values.getAsString("CreationDate"));
        insertStmt.bindString(2,values.getAsString("MemoContents"));
        insertStmt.bindString(3,values.getAsString("MemoTitle"));
        insertStmt.bindString(4,values.getAsString("YoutubeUrl"));
        insertStmt.bindBlob(5,values.getAsByteArray("Image"));
        insertStmt.bindString(6,values.getAsString("UseYN"));

        return insertStmt.executeInsert();
    }

    /**
     * key값을 이용해서 DB 데이터를 지움.
     * 완전히 삭제하지 않고 UseYN의 값을 Y에서 N으로 바꾼다.
     * @param key (int) - PK 값
     * @return 성공: 삭제된 열의 번호, 실패: -1
     */
    public int delete(int key){
        String where = dbHelper.COLUMN_ID + " = " + key;
        //db.delete(dbHelper.getTableName(),where, null); //완전삭제

        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_USEYN,"N");

        return db.update(TableName, values, where, null); //간접삭제
    }

    /**
     * key값을 이용해서 DB 데이터를 업데이트.
     * 한 번에 한 튜플만 수정할 수 있다.
     * @param key (int) PK 값
     * @param values (ContentValues- 여기서 키값은 어트리뷰트명과 동일) 업데이트할 정보들.
     * @return 성공: 수정된 열의 번호(long), 실패: -1
     */
    public long update(int key, ContentValues values){
        String sql = "UPDATE " + TableName+" SET ("
                + dbHelper.COLUMN_DATE +", "
                + dbHelper.COLUMN_CONTENTS+","
                + dbHelper.COLUMN_TITLE+","
                + dbHelper.COLUMN_YTBURL+","
                + dbHelper.COLUMN_IMG+") = (?, ?, ?, ?, ?)"
                +" WHERE "+ dbHelper.COLUMN_ID +"="+key;

        SQLiteStatement updateStmt = db.compileStatement(sql);
        updateStmt.clearBindings();
        updateStmt.bindString(1,values.getAsString("CreationDate"));
        updateStmt.bindString(2,values.getAsString("MemoContents"));
        updateStmt.bindString(3,values.getAsString("MemoTitle"));
        updateStmt.bindString(4,values.getAsString("YoutubeUrl"));
        updateStmt.bindBlob(5,values.getAsByteArray("Image"));

        return updateStmt.executeUpdateDelete();
    }

    /**
     * 날짜정보를 이용해 해당 날짜의 메모를 전부 select
     * @param date (String)
     * @return 성공: ArrayList<ContentValues> , 실패: null
     */
    public ArrayList<ContentValues> select(String date){
        String selection = dbHelper.COLUMN_DATE+"=? AND "+dbHelper.COLUMN_USEYN +"=?";
        Cursor cursor = db.query(TableName, null, selection, new String[]{date,"Y"}, null, null, null);

        if(!cursor.moveToFirst()){
            return null;
        }

        ArrayList<ContentValues> list= new ArrayList<>();
        while(true){
            ContentValues tmp = new ContentValues();

            tmp.put("Memo_ID",cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_ID)));
            tmp.put("CreationDate",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DATE)));
            tmp.put("MemoContents",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_CONTENTS)));
            tmp.put("MemoTitle",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_TITLE)));
            tmp.put("YoutubeUrl",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_YTBURL)));
            tmp.put("Image",cursor.getBlob(cursor.getColumnIndex(dbHelper.COLUMN_IMG)));
            tmp.put("UseYN",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_USEYN)));

            list.add(tmp);
            if(!cursor.moveToNext()) break;
        }
        return list;
    }

    /**
     * key값을 이용해 해당 메모정보를 반환.
     * @param key (int)
     * @return ContentValues 메모 정보가 담겨져 있음. HashMap.
     */
    public ContentValues selectOne(int key){
        String sql = "SELECT * FROM "+TableName+" WHERE "+dbHelper.COLUMN_ID+"="+key+" AND "+dbHelper.COLUMN_USEYN+"='Y'";
        Cursor cursor = db.rawQuery(sql,null);

        if(!cursor.moveToFirst()){
            return null;
        }

        ContentValues values = new ContentValues();

        values.put("Memo_ID",cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_ID)));
        values.put("CreationDate",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DATE)));
        values.put("MemoContents",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_CONTENTS)));
        values.put("MemoTitle",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_TITLE)));
        values.put("YoutubeUrl",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_YTBURL)));
        values.put("Image",cursor.getBlob(cursor.getColumnIndex(dbHelper.COLUMN_IMG)));
        values.put("UseYN",cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_USEYN)));

        return values;
    }

}
