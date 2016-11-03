package com.example.administrator.ipc_test;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MyUserManagerService extends Service {

    private static final String TAG = "MyUserManagerService";
    //支持并发读/写
    private CopyOnWriteArrayList<MyUser> userList = new CopyOnWriteArrayList<>();
    private IBinder iBinder = new IMyUserManager.Stub(){
        @Override
        public void addUser(MyUser user) throws RemoteException {
            if(!userList.contains(user)){
                userList.add(user);
            }
        }
        @Override
        public void removeUser(MyUser user) throws RemoteException {
            if(userList.contains(user)){
                userList.remove(user);
            }
        }
        @Override
        public int getUserNum() throws RemoteException {
            return userList.size();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //权限验证，通过返回IBinder,否则放回null
        int check = checkCallingOrSelfPermission("com.example.administrator.ipc_test.MyUserManagerService");
        Log.e(TAG, "check = " + check + ", PackageManager.PERMISSION_DENIED = " +
                PackageManager.PERMISSION_DENIED);
        if(check == PackageManager.PERMISSION_DENIED){
            return null;
        }
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userList.add(new MyUser());
    }
}
