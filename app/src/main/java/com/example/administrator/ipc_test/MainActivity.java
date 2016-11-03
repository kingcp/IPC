package com.example.administrator.ipc_test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.ipc_test.Messenger.MessengerActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private static final int MESSAGE_BINDER_POOL = 0;
    private static final int BINDER_ADD = 0;
    private static final int BINDER_REMOVE = 1;

    private IBookManager mRemoteBookManger;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "new book name : " + (String)msg.obj);
                    break;
                case MESSAGE_BINDER_POOL:
                    Toast.makeText(MainActivity.this,"size = "+BinderPoolService.list.size(),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub(){
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook.getmBookName()).sendToTarget();
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            mRemoteBookManger = bookManager;
            try {
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "list size " + list.size());
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteBookManger = null;
            Log.i(TAG, "mRemoteBookManger onServiceDisconnected");
        }
    };

    private Button btn;
    private ViewStub viewStub;
    private Button messenger_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messenger_btn = (Button) findViewById(R.id.messenger_btn);
        messenger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessengerActivity.class);
                startActivity(intent);
            }
        });
       /* Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
     /*   btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (findViewById(R.id.viewStub)).setVisibility(View.VISIBLE);
            }
        });
        Intent intent = new Intent(this,MyUserManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                Book book = new Book();
                BinderPool binderPool = BinderPool.getsInstance(MainActivity.this);
                IBinder iBinder = binderPool.queryBinder(BINDER_ADD);
                IAddBook addBook = IAddBookImpl.asInterface(iBinder);
                try {
                    addBook.add(book);
                    handler.obtainMessage(MESSAGE_BINDER_POOL, null).sendToTarget();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                iBinder = binderPool.queryBinder(BINDER_REMOVE);
                IRemoveBookImpl removeBook = (IRemoveBookImpl)IRemoveBookImpl.asInterface(iBinder);
                try {
                    removeBook.remove(book);
                    handler.obtainMessage(MESSAGE_BINDER_POOL,null).sendToTarget();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        if(mRemoteBookManger != null && mRemoteBookManger.asBinder().isBinderAlive()){
            try {
                mRemoteBookManger.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }


}
