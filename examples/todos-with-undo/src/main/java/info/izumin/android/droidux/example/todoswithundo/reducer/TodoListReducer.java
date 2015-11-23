package info.izumin.android.droidux.example.todoswithundo.reducer;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.annotation.Undoable;
import info.izumin.android.droidux.example.todoswithundo.action.AddTodoAction;
import info.izumin.android.droidux.example.todoswithundo.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.example.todoswithundo.action.DeleteTodoAction;
import info.izumin.android.droidux.example.todoswithundo.action.ToggleCompletedTodoAction;
import info.izumin.android.droidux.example.todoswithundo.entity.TodoList;
import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
@Undoable
@Reducer(TodoList.class)
public class TodoListReducer {
    public static final String TAG = TodoListReducer.class.getSimpleName();

    @Dispatchable(AddTodoAction.class)
    public TodoList onAddedTodo(TodoList state, AddTodoAction action) {
        int id = Observable.from(state)
                .reduce((todo, todo2) -> (todo.getId() < todo2.getId()) ? todo2 : todo)
                .onErrorReturn(throwable -> new TodoList.Todo(0, ""))
                .toBlocking().last().getId();
        TodoList newState = new TodoList(state);
        newState.add(new TodoList.Todo(id + 1, action.getText()));
        return newState;
    }

    @Dispatchable(ToggleCompletedTodoAction.class)
    public TodoList onCompletedTodo(TodoList state, ToggleCompletedTodoAction action) {
        return new TodoList(Observable.from(state)
                .map(todo -> {
                    if (todo.getId() == action.getId()) {
                        return new TodoList.Todo(todo.getId(), todo.getText(), !todo.isCompleted());
                    }
                    return todo;
                })
                .toList().toBlocking().single()
        );
    }

    @Dispatchable(ClearCompletedTodoAction.class)
    public TodoList onClearCompletedTodo(TodoList state, ClearCompletedTodoAction action) {
        return new TodoList(Observable.from(state)
                .filter(todo -> !todo.isCompleted()).toList().toBlocking().single()
        );
    }

    @Dispatchable(DeleteTodoAction.class)
    public TodoList onDeletedTodo(TodoList state, DeleteTodoAction action) {
        return new TodoList(Observable.from(state)
                .filter(todo -> todo.getId() != action.getId()).toList().toBlocking().single()
        );
    }
}
