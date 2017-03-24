package vaughandroid.daggertwospike;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MyActivitySubcomponent.class)
public abstract class MyActivityModule {

    @Binds @IntoMap @ActivityKey(MyActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(MyActivitySubcomponent.Builder builder);
}
