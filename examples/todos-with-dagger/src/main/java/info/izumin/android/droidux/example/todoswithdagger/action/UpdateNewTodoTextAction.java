package info.izumin.android.droidux.example.todoswithdagger.action;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 12/5/15.
 */
public class UpdateNewTodoTextAction implements Action {
    public static final String TAG = UpdateNewTodoTextAction.class.getSimpleName();

    private final String text;

    public UpdateNewTodoTextAction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
