package info.izumin.android.droidux.sample.reducer;

import java.util.List;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.sample.action.AddTodoAction;
import info.izumin.android.droidux.sample.entity.TodoList;
import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
@Reducer(TodoList.class)
public class TodoListReducer {
    public static final String TAG = TodoListReducer.class.getSimpleName();

    @Dispatchable(AddTodoAction.class)
    public TodoList onAddedTodo(TodoList state, AddTodoAction action) {
        List<TodoList.Todo> list = state.getTodoList();
        int id = Observable.from(list)
                .reduce((todo, todo2) -> (todo.getId() < todo2.getId()) ? todo2 : todo)
                .onErrorReturn(throwable -> new TodoList.Todo(0, ""))
                .toBlocking().last().getId();
        list.add(new TodoList.Todo(id, action.getText()));
        return new TodoList(list);
    }
}
