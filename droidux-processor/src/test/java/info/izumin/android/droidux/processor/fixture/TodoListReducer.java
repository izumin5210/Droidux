package info.izumin.android.droidux.processor.fixture;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.annotation.Undoable;
import info.izumin.android.droidux.processor.fixture.action.AddTodoItemAction;

/**
 * Created by izumin on 11/3/15.
 */
@Undoable
@Reducer(TodoList.class)
public class TodoListReducer {
    @Dispatchable(AddTodoItemAction.class)
    public TodoList add(TodoList state, AddTodoItemAction action) {
        TodoList newState = new TodoList();
        newState.addAll(state);
        newState.add(new TodoList.Item(action.getValue()));
        return newState;
    }
}
