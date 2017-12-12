package test.phantom.com.p90.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import java.util.Map;

import test.phantom.com.p90.base.live.JobHandlerService;
import test.phantom.com.p90.base.live.LocalService;
import test.phantom.com.p90.base.live.RemoteService;

/**
 * Created by jiang on 2017/3/9.
 */

public class CommonApplication extends Application {


    protected static final String TAG = "BaseApplication";
    // 用于存放倒计时时间
    public static Map<String, Long> map;
    private static CommonApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            openJobService();
        } else {
            openTwoService();
        }
        startService(new Intent(this, LocalService.class));
    }

    private void openTwoService() {
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
    }

    private void openJobService() {

        Intent intent = new Intent();
        intent.setClass(this, JobHandlerService.class);
        startService(intent);
    }

    public static Context getAppContext() {
        return instance;
    }

    public static Resources getAppResources() {
        return instance.getResources();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static CommonApplication getInstance() {
        return instance;
    }

    /**
     * 分包
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
