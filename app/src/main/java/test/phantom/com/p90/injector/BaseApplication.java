package test.phantom.com.p90.injector;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import test.phantom.com.p90.base.CommonApplication;


public class BaseApplication extends CommonApplication implements Thread.UncaughtExceptionHandler {

    private static BaseApplication application;
    private static ApplicationComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static ApplicationComponent getAppComponent() {
        return sAppComponent;
    }

    /**
     * 防止由于系统原因字体大小变化
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();

            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}



