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
import dagger.multibindings.IntoMap;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MyActivityTest {

    @Rule public ActivityTestRule<MyActivity> activityTestRule = new ActivityTestRule<>(MyActivity.class, false, false);

    private MyApp myApp;

    @Before public void setup() {
        myApp = (MyApp) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    @Test public void productionInjection() throws Exception {
        // Create production object graph & inject.
        DaggerAppComponent.builder()
                .appModule(new AppModule(myApp))
                .build()
                .inject(myApp);

        activityTestRule.launchActivity(new Intent(myApp, MyActivity.class));
        MyActivity myActivity = activityTestRule.getActivity();

        // Check things got injected correctly.
        Assert.assertEquals("singleton-value", myActivity.singletonString);
        Assert.assertEquals("per-activity-value", myActivity.perActivityString);
    }

    @Test public void mockInjection() throws Exception {
        // Create test object graph & inject.
        DaggerMyActivityTest_TestAppComponent.builder()
                .testAppModule(new TestAppModule(myApp))
                .build()
                .inject(myApp);

        activityTestRule.launchActivity(new Intent(myApp, MyActivity.class));
        MyActivity myActivity = activityTestRule.getActivity();

        // Check things got injected correctly.
        Assert.assertEquals("test-singleton-value", myActivity.singletonString);
        Assert.assertEquals("test-per-activity-value", myActivity.perActivityString);
    }

    @Singleton @Component(modules = { TestAppModule.class, AndroidInjectionModule.class, TestMyActivityModule.class })
    public interface TestAppComponent {

        void inject(MyApp myApp);
    }

    @Module
    public class TestAppModule {
        private final MyApp app;

        public TestAppModule(MyApp app) {
            this.app = app;
        }

        @Provides @Singleton MyApp app() {
            return app;
        }

        @Provides @Singleton @Named("singleton-string")
        public String singletonString() {
            return "test-singleton-value";
        }
    }

    @Module(subcomponents = TestMyActivitySubcomponent.class)
    public static abstract class TestMyActivityModule {

        @Binds @IntoMap @ActivityKey(MyActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bind(TestMyActivitySubcomponent.Builder builder);
    }

    @PerActivity @Subcomponent(modules = TestPerActivityModule.class)
    public interface TestMyActivitySubcomponent extends AndroidInjector<MyActivity> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<MyActivity> {}
    }

    @Module
    public static class TestPerActivityModule {

        @Provides
        @PerActivity @Named("per-activity-string")
        public String perActivityString() {
            return "test-per-activity-value";
        }
    }


}
