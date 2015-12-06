package info.izumin.android.droidux.example.todoswithdagger;

import android.app.Application;

import info.izumin.android.droidux.example.todoswithdagger.entity.TodoList;
import info.izumin.android.droidux.example.todoswithdagger.reducer.NewTodoTextReducer;
import info.izumin.android.droidux.example.todoswithdagger.reducer.TodoListReducer;

/**
 * Created by izumin on 11/4/15.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private RootStore store;
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupStore();
        setupGraph();
    }

    public RootStore getStore() {
        return store;
    }

    public AppComponent getComponent() {
        return component;
    }

    private void setupStore() {
        store = DroiduxRootStore.builder()
                .setReducer(new TodoListReducer(), new TodoList())
                .setReducer(new NewTodoTextReducer(), BR.newTodoText)
                .build();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
