package com.example.administrator.ipc_test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/7/29.
 */
public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    //用于跨进程的管理listener
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
            /*if(!mListenerList.contains(listener)){
                mListenerList.add(listener);
            }else{
                Log.i(TAG, "Listener already exist ");
            }
            Log.i(TAG, "registerListener , size: " + mListenerList.size());*/
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
           /* if(mListenerList.contains(listener)){
                mListenerList.remove(listener);
                Log.i(TAG, "unregister listener succeed");
            }else{
                Log.i(TAG, "not find can not register ");
            }
            Log.i(TAG, "unregisterListener, current size : " + mListenerList.size());*/
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class ServiceWorker implements Runnable{
        @Override
        public void run() {
            while(!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book book = new Book(bookId,"my road to android # " + bookId);
                mBookList.add(book);
                onNewBookArrived(book);
            }
        }
    }

    /**
     * 添加图书之后通知每个注册的Listener新书到了
     * @param book
     */
    private void onNewBookArrived(Book book){
        final int N = mListenerList.beginBroadcast();
        for (int i = 0 ; i < N ; i++){
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            if( listener != null){
                try {
                    listener.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
        /*for(int i = 0 ; i < mListenerList.size() ; i++){
            IOnNewBookArrivedListener listener = mListenerList.get(i);
            try {
                listener.onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }*/

    }

}
