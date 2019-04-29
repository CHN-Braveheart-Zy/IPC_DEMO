package com.braveheart_zy.myapplication;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    private CopyOnWriteArrayList<User> arrayList;

    @Override
    public void onCreate() {
        super.onCreate();
        arrayList = new CopyOnWriteArrayList<>();
        arrayList.add(new User("01","ZhanSan",18));
        arrayList.add(new User("02","LiSi",18));
        arrayList.add(new User("03","WangEr",18));
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
        return binder;
    }

    private IUserManager.Stub binder = new IUserManager.Stub() {
        @Override
        public List<User> getUserList() throws RemoteException {
            return arrayList;
        }

        @Override
        public void addUser(User user) throws RemoteException {
            arrayList.add(user);
        }
    };
}
