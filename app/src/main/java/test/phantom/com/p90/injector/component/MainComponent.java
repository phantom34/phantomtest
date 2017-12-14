package test.phantom.com.p90.injector.component;

import dagger.Component;
import test.phantom.com.p90.injector.PerActivity;
import test.phantom.com.p90.injector.module.MainModule;
import test.phantom.com.p90.ui.main.MainActivity;

/**
 * Created by phantom on 2017/12/13.
 */
@PerActivity
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
