package com.jisooZz.haru.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.jisooZz.haru.BaseApplication;
import com.jisooZz.haru.db.model.DiaryInfo;
import com.jisooZz.haru.db.model.UserInfo;

import java.util.ArrayList;
import java.util.List;


import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_CONTENT;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_DATE;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_ID;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_IMAGE;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_NO;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_PW;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_TITLE;
import static com.jisooZz.haru.db.DatabaseConstants.TABLE_DIARY;
import static com.jisooZz.haru.db.DatabaseConstants.TABLE_USERS;

public class DatabaseSource {

    public static final String TAG = DatabaseSource.class.getSimpleName().toString();

    private Context context;

    private SQLiteOpenHelper dbhelper;
    private SQLiteDatabase database;

    public DatabaseSource(Context context) {
        this.context = context;
        this.dbhelper = new DatabaseHelper(context);
    }

    public void open(){
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        dbhelper.close();
    }

    /**
     * 회원가입
     * */
    public boolean insertUser(UserInfo info){
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = '" + info.getId() + "'";
        Log.d(TAG, "sql : " + sql);
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) return false;

        ContentValues userData = new ContentValues();
        userData.put(COLUMN_ID, info.getId());
        userData.put(COLUMN_PW, info.getPassword());
        database.insert(TABLE_USERS, null, userData);
        return true;
    }

    /**
     * 회원 등록 조회 여부
     * */
    public boolean checkUserExists(UserInfo info){
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = '" + info.getId()
                + "' AND " + COLUMN_PW + " = '" + info.getPassword() + "'";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) return true;
        return false;
    }

    /**
     * 내가 등록한 다이어리 조회
     * */
    public List<DiaryInfo> getMyDiary() {
        String sql = "SELECT * FROM " + TABLE_DIARY + " WHERE " + COLUMN_ID + " = '" + getUserId() + "'" +
                " ORDER BY " + COLUMN_DATE + " DESC";
        Log.d(TAG, "sql : " + sql);

        Cursor cursor = database.rawQuery(sql, null);
        List<DiaryInfo> result = cursorToList(cursor);
        return result;
    }

    /**
     * 클릭한 다이어리 내용 가져오기
     * */
    public DiaryInfo getTheDiary(int no) {
        String sql = "SELECT * FROM " + TABLE_DIARY + " WHERE " + COLUMN_NO + " = '" + no + "' AND " + COLUMN_ID + " = '" + getUserId() + "'";
        Log.d(TAG, "sql : " + sql);

        DiaryInfo info = null;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                info = new DiaryInfo();
                info.setNo(cursor.getInt(cursor.getColumnIndex(COLUMN_NO)));
                info.setDdate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                info.setDtitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                info.setDcontent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                info.setDimg(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                info.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            }
        }
        return info;
    }

    /**
     * 다이어리 등록
     * */
    public void inserDiary(DiaryInfo info) {
        if (info != null) {
            ContentValues diaryData = new ContentValues();
            diaryData.put(COLUMN_DATE, info.getDdate());
            diaryData.put(COLUMN_TITLE, info.getDtitle());
            diaryData.put(COLUMN_CONTENT, info.getDcontent());
            diaryData.put(COLUMN_IMAGE, info.getDimg());
            diaryData.put(COLUMN_ID, getUserId());
            database.insert(TABLE_DIARY, null, diaryData);
        }
    }
    /**
     * 다이어리 수정
     * */
    public void updateDiary(DiaryInfo info, int no) {
        String sql = "UPDATE " + TABLE_DIARY + " SET "
                + COLUMN_DATE + " = '" + info.getDdate() + "' , "
                + COLUMN_TITLE + " = '" + info.getDtitle() + "' , "
                + COLUMN_CONTENT + " = '" + info.getDcontent() + "' , "
                + COLUMN_IMAGE + " = '" + info.getDimg() + "' WHERE "
                + COLUMN_NO + " = '" + no + "'";
        Log.d(TAG, "sql : " + sql);

        database.execSQL(sql);
    }

    /**
     * 다이어리 삭제
     * */
    public void deleteDiary(int no){
        String sql = "DELETE FROM " + TABLE_DIARY + " WHERE " + COLUMN_NO + " = '" + no + "' AND " + COLUMN_ID + " = '" +
                BaseApplication.getInstance(context).getUserId() + "'";
        Log.d(TAG, "sql : " + sql);

        database.execSQL(sql);
    }

    private List<DiaryInfo> cursorToList(Cursor cursor) {
        List<DiaryInfo> diaryInfo = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DiaryInfo diary = new DiaryInfo();
                diary.setNo(cursor.getInt(cursor.getColumnIndex(COLUMN_NO)));
                diary.setDdate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                diary.setDtitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                diary.setDcontent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                diary.setDimg(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                diary.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                diaryInfo.add(diary);
            }
        }
        return diaryInfo;
    }

    /**
     * 로그인한 회원 아이디 가져오기
     * */
    private String getUserId() {
        if (BaseApplication.getInstance(context).getUserId() != null) {
            return BaseApplication.getInstance(context).getUserId();
        }
        return "";
    }
}
