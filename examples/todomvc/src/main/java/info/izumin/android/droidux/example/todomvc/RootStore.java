package info.izumin.android.droidux.example.todomvc;

import info.izumin.android.droidux.BaseStore;
import info.izumin.android.droidux.annotation.Store;
import info.izumin.android.droidux.example.todomvc.entity.TodoList;
import info.izumin.android.droidux.example.todomvc.reducer.TodoListReducer;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by izumin on 11/29/15.
 */
@Store(TodoListReducer.class)
public interface RootStore extends BaseStore {
    TodoList todoList();
    Flowable<TodoList> observeTodoList();
    Flowable<TodoList> observeTodoList(BackpressureStrategy strategy);
}
