package com.example.administrator.ipc_test.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/3.
 */
public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.example.administrator.ipc_test.book.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: current thread : " + Thread.currentThread().getName());
        mContext = getContext();
        //onCreate 运行在主线程中，耗时操作要在子线程中进行
        initProviderData();
        return true;
    }

    private void initProviderData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb = new DbOpenHelper(mContext).getWritableDatabase();
                mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
                mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
                mDb.execSQL("insert into " + DbOpenHelper.BOOK_TABLE_NAME + " values(3,'Android')");
                mDb.execSQL("insert into " + DbOpenHelper.BOOK_TABLE_NAME + " values(4,'IOS')");
                mDb.execSQL("insert into " + DbOpenHelper.BOOK_TABLE_NAME + " values(5,'Html5')");
                mDb.execSQL("insert into " + DbOpenHelper.USER_TABLE_NAME + " values(1,'jack',1)");
                mDb.execSQL("insert into " + DbOpenHelper.USER_TABLE_NAME + " values(2,'rose',0)");
            }
        }).start();
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: current thread : " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if(null == table){
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType: ");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert: ");
        String table = getTableName(uri);
        if(null == table){
            throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        mDb.insert(table,null,values);
        //通知外界当前ContentProvider中的数据已经发生变化
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete: ");
        String table = getTableName(uri);
        if(null == table){
            throw new IllegalArgumentException("Unsupported URI " + uri );
        }
        int count = mDb.delete(table, selection, selectionArgs);
        if(count > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update ");
        String table = getTableName(uri);
        if(null == table){
            throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        int row = mDb.update(table,values,selection,selectionArgs);
        if(row > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    private String getTableName(Uri uri){
        String tableName = null;
        switch (sUriMatcher.match(uri)){
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:break;
        }
        return tableName;
    }
}