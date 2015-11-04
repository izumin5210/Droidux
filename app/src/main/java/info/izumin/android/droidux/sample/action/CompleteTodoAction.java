package info.izumin.android.droidux.sample.action;

import com.google.gson.Gson;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/4/15.
 */
public class CompleteTodoAction extends Action {
    public static final String TAG = CompleteTodoAction.class.getSimpleName();

    private final int id;
    private final boolean isCompleted;

    public CompleteTodoAction(int id, boolean isCompleted) {
        this.id = id;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
