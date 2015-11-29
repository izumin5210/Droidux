package info.izumin.android.droidux.example.todoswithdagger.module.main;

import dagger.Subcomponent;

/**
 * Created by izumin on 11/29/15.
 */
@Subcomponent(
        modules = MainActivityModule.class
)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
