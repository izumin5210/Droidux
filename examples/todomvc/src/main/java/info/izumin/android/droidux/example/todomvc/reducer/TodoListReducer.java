package info.izumin.android.droidux.example.todomvc.reducer;

import java.util.List;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.example.todomvc.action.AddTodoAction;
import info.izumin.android.droidux.example.todomvc.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.example.todomvc.action.DeleteTodoAction;
import info.izumin.android.droidux.example.todomvc.action.ToggleCompletedTodoAction;
import info.izumin.android.droidux.example.todomvc.entity.TodoList;
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
        list.add(new TodoList.Todo(id + 1, action.getText()));
        return new TodoList(list);
    }

    @Dispatchable(ToggleCompletedTodoAction.class)
    public TodoList onCompletedTodo(TodoList state, ToggleCompletedTodoAction action) {
        return new TodoList(Observable.from(state.getTodoList())
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
        return new TodoList(Observable.from(state.getTodoList())
                .filter(todo -> !todo.isCompleted()).toList().toBlocking().single()
        );
    }

    @Dispatchable(DeleteTodoAction.class)
    public TodoList onDeletedTodo(TodoList state, DeleteTodoAction action) {
        return new TodoList(Observable.from(state.getTodoList())
                .filter(todo -> todo.getId() != action.getId()).toList().toBlocking().single()
        );
    }
}
