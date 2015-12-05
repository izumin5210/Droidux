package info.izumin.android.droidux.example.todoswithdagger;

import javax.inject.Singleton;

import dagger.Component;
import info.izumin.android.droidux.example.todoswithdagger.module.main.MainComponent;
import info.izumin.android.droidux.example.todoswithdagger.module.main.MainModule;

/**
 * Created by izumin on 11/29/15.
 */
@Singleton
@Component(
        modules = AppModule.class
)
public interface AppComponent {
    MainComponent createMainActivityComponent(MainModule module);
}
