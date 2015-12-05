package info.izumin.android.droidux.example.todoswithundo;

import android.app.Application;

import info.izumin.android.droidux.example.todoswithundo.entity.TodoList;
import info.izumin.android.droidux.example.todoswithundo.middleware.Logger;
import info.izumin.android.droidux.example.todoswithundo.reducer.TodoListReducer;

/**
 * Created by izumin on 11/4/15.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private RootStore store;

    @Override
    public void onCreate() {
        super.onCreate();
        store = DroiduxRootStore.builder()
                .addMiddleware(new Logger())
                .setReducer(new TodoListReducer(), new TodoList())
                .build();
    }

    public RootStore getStore() {
        return store;
    }
}
