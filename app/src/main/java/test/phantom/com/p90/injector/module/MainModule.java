package test.phantom.com.p90.injector.module;

import dagger.Module;
import dagger.Provides;
import test.phantom.com.p90.injector.PerActivity;
import test.phantom.com.p90.ui.main.MainActivity;

/**
 * @author phantom
 * @date 2017/12/13
 */

@Module
public class MainModule {
    private  final MainActivity mView;

    public MainModule(MainActivity view) {
        this.mView = view;
    }

    @Provides
    @PerActivity
    public MainActivity provideview() {
        return mView;
    }

}


