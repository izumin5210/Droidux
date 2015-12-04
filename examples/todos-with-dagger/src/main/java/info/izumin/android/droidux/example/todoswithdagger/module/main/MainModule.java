package info.izumin.android.droidux.example.todoswithdagger.module.main;

import dagger.Module;
import dagger.Provides;
import info.izumin.android.droidux.example.todoswithdagger.RootStore;

/**
 * Created by izumin on 11/29/15.
 */
@Module
public class MainModule {
    public static final String TAG = MainModule.class.getSimpleName();

    private final MainActivity activity;

    public MainModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    MainPresenter provideMainPresenter(RootStore store) {
        return new MainPresenter(activity, store);
    }
}
