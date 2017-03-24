package vaughandroid.daggertwospike;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PerActivityModule {

    @Provides @PerActivity @Named("per-activity-string")
    public String perActivityString() {
        return "per-activity-value";
    }
}
