package com.example.braveheart_zy.ipc_demo_messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button btnBindService;
    private Button btnMethod1;
    private Button btnMethod2;
    private Button btnUnbindService;
    private Messenger messenger;

    private static class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.MSG_FROM_SERVER) {
                String replay = msg.getData().getString("reply");
                Log.d(TAG," from service: " + replay);
            }
        }
    }
    private Messenger replyMessenger = new Messenger(new ClientHandler());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBindService = findViewById(R.id.bind_service);
        btnMethod1 = findViewById(R.id.method1);
        btnMethod2 = findViewById(R.id.method2);
        btnUnbindService = findViewById(R.id.unbind_service);

        btnBindService.setOnClickListener(this);
        btnMethod1.setOnClickListener(this);
        btnMethod2.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        if (v == btnBindService) {
            Intent intent = new Intent(this, RemoteService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } else if (v == btnMethod1) {
            if (messenger != null) {
                Message obtain = Message.obtain();
                obtain.what = Constant.MSG_FROM_CLIENT_METHOD_1;
                Bundle bundle = new Bundle();
                bundle.putString("key","from client");
                obtain.setData(bundle);
                try {
                    messenger.send(obtain);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnMethod2) {
            if (messenger != null) {
                Message obtain = Message.obtain();
                obtain.what = Constant.MSG_FROM_CLIENT_METHOD_2;
                Bundle bundle = new Bundle();
                bundle.putString("key","from client");
                obtain.setData(bundle);
                obtain.replyTo = replyMessenger;
                try {
                    messenger.send(obtain);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnUnbindService) {
            unbindService(connection);
        }
    }
}
