package com.braveheart_zy.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Button btnBindService;
    private Button btnMethod1;
    private Button btnMethod2;
    private Button btnUnbindService;
    private IUserManager userManager;

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
            userManager = IUserManager.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: ");
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
            if (userManager != null) {
                try {
                    userManager.addUser(new User("03", "LeiHou", 20));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnMethod2) {
            if (userManager != null) {
                try {
                    List<User> userList = userManager.getUserList();
                    Toast.makeText(this, userList.toString(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnUnbindService) {
            unbindService(connection);
        }
    }
}
