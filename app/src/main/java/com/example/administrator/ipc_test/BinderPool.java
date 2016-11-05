package com.example.administrator.ipc_test;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2016/8/18.
 */
public class BinderPool {

    private static final String TAG = "BinderPool";
    private static final int BINDER_NONE = -1;
    public static final int BINDER_ADD = 0;
    public static final int BINDER_REMOVE = 1;
    public static final int BINDER_SECURITY_CENTER = 2;
    public static final int BINDER_COMPUTE = 3;

    private Context context;
    private IBinderPool iBinderPool;
    private static volatile BinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                iBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }finally {
                mConnectBinderPoolCountDownLatch.countDown();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //Binder连接死掉
    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            iBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            iBinderPool = null;
            connectBinderPoolService();
        }
    };

    private BinderPool(Context context){
        this.context = context;
        connectBinderPoolService();
    }

    public static BinderPool getsInstance(Context context){
        if(sInstance == null){
            synchronized (BinderPool.class){
                if(sInstance == null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    //连接Binder线程池
    private synchronized void connectBinderPoolService(){
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(context,BinderPoolService.class);
        context.bindService(intent,mBinderPoolConnection, Service.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if(iBinderPool != null){
                binder = iBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
                e.printStackTrace();
        }
        return binder;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub{
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_ADD:
                    binder = new IAddBookImpl();
                    break;
                case BINDER_REMOVE:
                    binder = new IRemoveBookImpl();
                    break;
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
            }
            return binder;
        }
    }

}
