package vaughandroid.daggertwospike;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final MyApp app;

    public AppModule(MyApp app) {
        this.app = app;
    }

    @Provides @Singleton MyApp app() {
        return app;
    }

    @Provides @Singleton @Named("singleton-string")
    public String singletonString() {
        return "singleton-value";
    }
}
