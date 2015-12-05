package info.izumin.android.droidux.example.todoswithdagger;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.annotation.Store;
import info.izumin.android.droidux.example.todoswithdagger.entity.TodoList;
import info.izumin.android.droidux.example.todoswithdagger.reducer.NewTodoTextReducer;
import info.izumin.android.droidux.example.todoswithdagger.reducer.TodoListReducer;
import rx.Observable;

/**
 * Created by izumin on 11/29/15.
 */
@Store({TodoListReducer.class, NewTodoTextReducer.class})
public interface RootStore {
    TodoList todoList();
    String newTodoText();
    Observable<TodoList> observeTodoList();
    Observable<Action> dispatch(Action action);
}
