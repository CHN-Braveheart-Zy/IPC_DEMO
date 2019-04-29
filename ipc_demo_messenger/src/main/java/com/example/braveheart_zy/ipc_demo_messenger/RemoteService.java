package com.example.braveheart_zy.ipc_demo_messenger;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Log.d(TAG, "bindService");
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Toast.makeText(this, "unbind", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "unbindService");
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service destory", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private static class MessengerHandler extends  Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.MSG_FROM_CLIENT_METHOD_1) {
                Bundle bundle = msg.getData();
                Log.d(TAG,"msg=" + bundle.getString("key"));
            } else if (msg.what == Constant.MSG_FROM_CLIENT_METHOD_2) {
                Messenger client = msg.replyTo;
                Message message = Message.obtain(null, Constant.MSG_FROM_SERVER);
                Bundle data = new Bundle();
                data.putString("reply","from Server");
                message.setData(data);
                try {
                    client.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());
}
