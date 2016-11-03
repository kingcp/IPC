package com.example.administrator.ipc_test;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Administrator on 2016/8/18.
 */
public class IAddBookImpl extends IAddBook.Stub {
    @Override
    public void add(Book book) throws RemoteException {
        Log.e("BINDER_POOL", "add: ");
        if(!BinderPoolService.list.contains(book)){
            BinderPoolService.list.add(book);
        }
    }
}
