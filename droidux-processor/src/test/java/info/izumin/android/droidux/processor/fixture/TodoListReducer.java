package info.izumin.android.droidux.processor.fixture;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;

/**
 * Created by izumin on 11/3/15.
 */
@Reducer(TodoList.class)
public class TodoListReducer {
    @Dispatchable(AddTodoItemAction.class)
    public TodoList onAddItem(TodoList state) {
        return state;
    }
    @Dispatchable(CompleteTodoItemAction.class)
    public TodoList onCompleteItem(TodoList state) {
        return state;
    }
}
