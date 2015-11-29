package info.izumin.android.droidux.example.todoswithdagger.action;

import com.google.gson.Gson;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/5/15.
 */
public class ToggleCompletedTodoAction implements Action {
    public static final String TAG = ToggleCompletedTodoAction.class.getSimpleName();

    private final int id;

    public ToggleCompletedTodoAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
