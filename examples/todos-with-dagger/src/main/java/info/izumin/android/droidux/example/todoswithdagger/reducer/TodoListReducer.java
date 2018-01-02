package info.izumin.android.droidux.example.todoswithdagger.reducer;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.example.todoswithdagger.action.AddTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.DeleteTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ToggleCompletedTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.entity.TodoList;
import io.reactivex.Observable;

/**
 * Created by izumin on 11/4/15.
 */
@Reducer(TodoList.class)
public class TodoListReducer {
    public static final String TAG = TodoListReducer.class.getSimpleName();

    @Dispatchable(AddTodoAction.class)
    public TodoList add(TodoList state, AddTodoAction action) {
        int id = Observable.fromIterable(state)
                .reduce((todo, todo2) -> (todo.getId() < todo2.getId()) ? todo2 : todo)
                .onErrorReturn(throwable -> new TodoList.Todo(0, ""))
                .toObservable()
                .blockingLast()
                .getId();
        TodoList newState = new TodoList(state);
        newState.add(new TodoList.Todo(id + 1, action.getText()));
        return newState;
    }

    @Dispatchable(ToggleCompletedTodoAction.class)
    public TodoList complete(TodoList state, ToggleCompletedTodoAction action) {
        return new TodoList(Observable.fromIterable(state)
                .map(todo -> {
                    if (todo.getId() == action.getId()) {
                        return new TodoList.Todo(todo.getId(), todo.getText(), !todo.isCompleted());
                    }
                    return todo;
                })
                .toList().blockingGet()
        );
    }

    @Dispatchable(ClearCompletedTodoAction.class)
    public TodoList clear(TodoList state) {
        return new TodoList(Observable.fromIterable(state)
                .filter(todo -> !todo.isCompleted()).toList().blockingGet()
        );
    }

    @Dispatchable(DeleteTodoAction.class)
    public TodoList delete(TodoList state, DeleteTodoAction action) {
        return new TodoList(Observable.fromIterable(state)
                .filter(todo -> todo.getId() != action.getId()).toList().blockingGet()
        );
    }
}
