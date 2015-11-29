package info.izumin.android.droidux.processor.fixture.action;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/2/15.
 */
public class CompleteTodoItemAction implements Action {
    public static final String TAG = CompleteTodoItemAction.class.getSimpleName();

    private final int id;

    public CompleteTodoItemAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
