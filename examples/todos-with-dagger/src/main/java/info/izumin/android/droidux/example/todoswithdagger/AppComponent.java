package info.izumin.android.droidux.example.todoswithdagger;

import javax.inject.Singleton;

import dagger.Component;
import info.izumin.android.droidux.example.todoswithdagger.module.main.MainActivityComponent;
import info.izumin.android.droidux.example.todoswithdagger.module.main.MainActivityModule;

/**
 * Created by izumin on 11/29/15.
 */
@Singleton
@Component(
        modules = AppModule.class
)
public interface AppComponent {
    MainActivityComponent createMainActivityComponent(MainActivityModule module);
}
