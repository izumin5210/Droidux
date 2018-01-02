package info.izumin.android.droidux.example.todoswithundo;

import info.izumin.android.droidux.BaseStore;
import info.izumin.android.droidux.annotation.Store;
import info.izumin.android.droidux.example.todoswithundo.entity.TodoList;
import info.izumin.android.droidux.example.todoswithundo.reducer.TodoListReducer;
import io.reactivex.Observable;

/**
 * Created by izumin on 11/29/15.
 */
@Store(TodoListReducer.class)
public interface RootStore extends BaseStore {
    TodoList todoList();
    Observable<TodoList> observeTodoList();
}
