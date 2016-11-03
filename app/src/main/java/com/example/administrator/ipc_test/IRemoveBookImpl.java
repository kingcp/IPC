package com.example.administrator.ipc_test;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Administrator on 2016/8/18.
 */
public class IRemoveBookImpl extends IRemoveBook.Stub {
    @Override
    public void remove(Book book) throws RemoteException {
        Log.e("BINDER_POOL", "remove: ");
        if(BinderPoolService.list.contains(book))
            BinderPoolService.list.remove(book);
    }
}
