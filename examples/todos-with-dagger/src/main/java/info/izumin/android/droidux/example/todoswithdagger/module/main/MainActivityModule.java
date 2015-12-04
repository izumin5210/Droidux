package info.izumin.android.droidux.example.todoswithdagger.module.main;

import dagger.Module;
import dagger.Provides;
import info.izumin.android.droidux.example.todoswithdagger.RootStore;

/**
 * Created by izumin on 11/29/15.
 */
@Module
public class MainActivityModule {
    public static final String TAG = MainActivityModule.class.getSimpleName();

    private final MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    MainActivityHelper provideMainActivityHelper(RootStore store) {
        return new MainActivityHelper(activity, store);
    }

}
