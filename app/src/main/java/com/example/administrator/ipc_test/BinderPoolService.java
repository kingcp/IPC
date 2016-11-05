package com.example.administrator.ipc_test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */
public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";
    public static CopyOnWriteArrayList<Book> list = new CopyOnWriteArrayList<>();
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind ");
        return mBinderPool;
    }
}
