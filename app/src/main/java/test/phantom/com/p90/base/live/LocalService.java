package test.phantom.com.p90.base.live;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jiang on 2017/10/17.
 */

public class LocalService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AliveBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bindService(new Intent(LocalService.this,RemoteService.class),mServiceConnection,BIND_AUTO_CREATE);

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindService(new Intent(LocalService.this,RemoteService.class),mServiceConnection,BIND_AUTO_CREATE);
        }
    };
}
