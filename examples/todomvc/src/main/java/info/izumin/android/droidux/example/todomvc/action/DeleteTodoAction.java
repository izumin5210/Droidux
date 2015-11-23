package info.izumin.android.droidux.example.todomvc.action;

import com.google.gson.Gson;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/5/15.
 */
public class DeleteTodoAction extends Action {
    public static final String TAG = DeleteTodoAction.class.getSimpleName();

    private final long id;

    public DeleteTodoAction(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
