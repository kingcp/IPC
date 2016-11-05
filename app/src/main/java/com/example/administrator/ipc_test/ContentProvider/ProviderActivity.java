package com.example.administrator.ipc_test.ContentProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.ipc_test.Book;
import com.example.administrator.ipc_test.R;

/**
 * Created by Administrator on 2016/11/3.
 */
public class ProviderActivity extends Activity {

    private static final String TAG = "ProviderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger_layout);
        ((TextView)findViewById(R.id.content_tv)).setText("ContentProvider实验");
        Uri uri = Uri.parse("content://com.example.administrator.ipc_test.book.provider");
        getContentResolver().query(uri,null,null,null,null);
        getContentResolver().query(uri,null,null,null,null);
        getContentResolver().query(uri,null,null,null,null);
        /**------------------------------------------------------------------------**/
        Uri bookUri = Uri.parse("content://com.example.administrator.ipc_test.book.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name", "半生为人");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri,new String[]{"_id","name"},null,null,null);
        while(bookCursor.moveToNext()){
            Book book = new Book();
            book.setmBookId(bookCursor.getInt(bookCursor.getColumnIndex("_id")));
            book.setmBookName(bookCursor.getString(bookCursor.getColumnIndex("name")));
            Log.d(TAG, "query book : " + book.toString());
        }
        bookCursor.close();
        /**------------------------------------------------------------------------**/
        Uri userUri = Uri.parse("content://com.example.administrator.ipc_test.book.provider/user");
        Cursor userCursor = getContentResolver().query(userUri,new String[]{"_id","name","sex"},null,null,null);
        while(userCursor.moveToNext()){
            User user = new User();
            user.setId(userCursor.getInt(userCursor.getColumnIndex("_id")));
            user.setName(userCursor.getString(userCursor.getColumnIndex("name")));
            user.setSex(userCursor.getInt(userCursor.getColumnIndex("sex")));
            Log.d(TAG, "query user : " + user.toString());
        }
        userCursor.close();
    }
}
