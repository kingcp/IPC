package com.example.administrator.ipc_test;

import android.os.RemoteException;

/**
 * Created by Administrator on 2016/11/4.
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
