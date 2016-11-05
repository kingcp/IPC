package com.example.administrator.ipc_test.ContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/4.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";
    private static final int DB_VERSION = 1;

    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME + " (_id INTEGER PRIMARY KEY,name TEXT)";
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (_id INTEGER PRIMARY KEY,name TEXT,sex INT)";
    private String DELETE_BOOK_TABLE = "DROP TABLE IF EXISTS " + BOOK_TABLE_NAME;
    private String DELETE_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_BOOK_TABLE);
        db.execSQL(DELETE_USER_TABLE);
        this.onCreate(db);
    }
}
