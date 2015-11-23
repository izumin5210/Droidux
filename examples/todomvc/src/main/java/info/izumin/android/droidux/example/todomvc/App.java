package info.izumin.android.droidux.example.todomvc;

import android.app.Application;

import java.util.ArrayList;

import info.izumin.android.droidux.example.todomvc.entity.TodoList;
import info.izumin.android.droidux.example.todomvc.middleware.Logger;
import info.izumin.android.droidux.example.todomvc.reducer.DroiduxRootStore;
import info.izumin.android.droidux.example.todomvc.reducer.TodoListReducer;

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
                .setReducer(new TodoListReducer())
                .setInitialState(new TodoList(new ArrayList<>()))
                .build();
    }

    public DroiduxRootStore getStore() {
        return store;
    }
}
