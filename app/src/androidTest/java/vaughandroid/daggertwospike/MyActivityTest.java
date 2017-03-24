package vaughandroid.daggertwospike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.multibindings.IntoMap;

import static org.junit.Assert.*;

/**
 * Example of the "official" way to inject Android framework classes on a per-test basis.
 */
@RunWith(AndroidJUnit4.class)
public class MyActivityTest {

    @Rule public ActivityTestRule<MyActivity> activityTestRule = new ActivityTestRule<>(MyActivity.class, false, false);

    private MyApp myApp;

    @Before public void setup() {
        myApp = (MyApp) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    @Test public void mockInjection() throws Exception {
        // Mock the injector and set it in the App.
        DispatchingAndroidInjector<Activity> mockInjector = (DispatchingAndroidInjector<Activity>) Mockito.mock(DispatchingAndroidInjector.class);
        myApp.dispatchingAndroidInjector = mockInjector;

        activityTestRule.launchActivity(new Intent(myApp, MyActivity.class));
        MyActivity myActivity = activityTestRule.getActivity();

        // Check things got injected correctly.
        Assert.assertEquals("test-singleton-value", myActivity.singletonString);
        Assert.assertEquals("test-per-activity-value", myActivity.perActivityString);
    }

    @Singleton @Component(modules = { TestAppModule.class, AndroidInjectionModule.class })
    public interface TestAppComponent {

        void inject(MyApp myApp);
    }

    @Module
    public static class TestAppModule {
        @Provides @IntoMap @ActivityKey(MyActivity.class)
        AndroidInjector.Factory<? extends Activity> factory() {
            return new AndroidInjector.Factory<MyActivity>() {
                @Override
                public AndroidInjector<MyActivity> create(MyActivity instance) {
                    return new AndroidInjector<MyActivity>() {
                        @Override
                        public void inject(MyActivity instance) {
                            instance.singletonString = "test-singleton-value";
                            instance.perActivityString = "test-per-activity-value";
                        }
                    };
                }
            };
        }
    }

}
