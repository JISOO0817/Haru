package com.jisooZz.haru.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_CONTENT;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_DATE;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_ID;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_IMAGE;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_NO;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_PW;
import static com.jisooZz.haru.db.DatabaseConstants.COLUMN_TITLE;
import static com.jisooZz.haru.db.DatabaseConstants.DATABASE_NAME;
import static com.jisooZz.haru.db.DatabaseConstants.DATABASE_VERSION;
import static com.jisooZz.haru.db.DatabaseConstants.TABLE_DIARY;
import static com.jisooZz.haru.db.DatabaseConstants.TABLE_USERS;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName().toString();

    // -- SQLiteOpenHelper 클래스를 상속한 DatabaseHelper 클래스 정의
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // -- 데이터베이스 테이블 만듦
    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_USER_CREATE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_PW + " TEXT" +
                ")";
        String TABLE_DIARY_CREATE =  "CREATE TABLE " + TABLE_DIARY + " (" +
                COLUMN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " DATETIME, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_ID + " TEXT" +
                ")";

         db.execSQL(TABLE_USER_CREATE);
         db.execSQL(TABLE_DIARY_CREATE);
        Log.i(TAG, "Table is created.");
    }

    // 데이터베이스를 업그레이드할 때 호출
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
        onCreate(db);
        Log.i(TAG, "Database has been upgraded from " + oldVersion + " to " + newVersion);
    }
}
