package info.izumin.android.droidux.example.todoswithdagger;

import android.databinding.Bindable;

import info.izumin.android.droidux.BaseStore;
import info.izumin.android.droidux.annotation.Store;
import info.izumin.android.droidux.example.todoswithdagger.entity.TodoList;
import info.izumin.android.droidux.example.todoswithdagger.reducer.NewTodoTextReducer;
import info.izumin.android.droidux.example.todoswithdagger.reducer.TodoListReducer;
import rx.Observable;

/**
 * Created by izumin on 11/29/15.
 */
@Store({TodoListReducer.class, NewTodoTextReducer.class})
public interface RootStore extends BaseStore {
    TodoList todoList();
    Observable<TodoList> observeTodoList();
    @Bindable String getNewTodoText();
}
