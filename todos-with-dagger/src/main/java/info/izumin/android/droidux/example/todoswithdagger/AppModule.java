package info.izumin.android.droidux.example.todoswithdagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by izumin on 11/29/15.
 */
@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    RootStore provideRootStore() {
        return app.getStore();
    }
}
