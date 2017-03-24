package vaughandroid.daggertwospike;


import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton @Component(modules = { AppModule.class, AndroidInjectionModule.class, MyActivityModule.class })
public interface AppComponent {

    void inject(MyApp myApp);
}
