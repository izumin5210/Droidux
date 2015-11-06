package info.izumin.android.droidux.sample;

import android.app.Application;

import java.util.ArrayList;

import info.izumin.android.droidux.sample.entity.TodoList;
import info.izumin.android.droidux.sample.middleware.Logger;
import info.izumin.android.droidux.sample.reducer.DroiduxRootStore;
import info.izumin.android.droidux.sample.reducer.TodoListReducer;

/**
 * Created by izumin on 11/4/15.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private DroiduxRootStore store;

    @Override
    public void onCreate() {
        super.onCreate();
        store = new DroiduxRootStore.Builder()
                .addMiddleware(new Logger())
                .addReducer(new TodoListReducer())
                .addInitialState(new TodoList(new ArrayList<>()))
                .build();
    }

    public DroiduxRootStore getStore() {
        return store;
    }
}
