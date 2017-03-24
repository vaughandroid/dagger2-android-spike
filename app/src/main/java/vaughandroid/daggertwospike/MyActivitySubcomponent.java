package vaughandroid.daggertwospike;


import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity @Subcomponent(modules = PerActivityModule.class)
public interface MyActivitySubcomponent extends AndroidInjector<MyActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MyActivity> {}
}
