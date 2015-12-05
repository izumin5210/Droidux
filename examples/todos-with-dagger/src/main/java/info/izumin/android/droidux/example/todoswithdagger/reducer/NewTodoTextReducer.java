package info.izumin.android.droidux.example.todoswithdagger.reducer;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.example.todoswithdagger.action.ClearNewTodoTextAction;
import info.izumin.android.droidux.example.todoswithdagger.action.UpdateNewTodoTextAction;

/**
 * Created by izumin on 12/5/15.
 */
@Reducer(String.class)
public class NewTodoTextReducer {
    public static final String TAG = NewTodoTextReducer.class.getSimpleName();

    @Dispatchable(UpdateNewTodoTextAction.class)
    public String update(UpdateNewTodoTextAction action) {
        return action.getText();
    }

    @Dispatchable(ClearNewTodoTextAction.class)
    public String clear() {
        return "";
    }
}
