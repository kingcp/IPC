// IBinderPool.aidl
package com.example.administrator.ipc_test;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
