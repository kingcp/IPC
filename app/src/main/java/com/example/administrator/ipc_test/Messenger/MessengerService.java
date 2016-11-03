package com.example.administrator.ipc_test.Messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 服务端
 * Created by Administrator on 2016/11/2.
 */
public class MessengerService extends Service {

    public static final int MSG_FROM_CLIENT = 1;
    public static final int MSG_FROM_SERVICE = 2;
    private static final String TAG = "MessengerService";
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_FROM_CLIENT:
                    Log.i(TAG, "receive msg from Client:"+ msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null,MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","嗯，你的消息我已经收到，稍后会回复你。");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private final Messenger mMessenger = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
