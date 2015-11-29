package info.izumin.android.droidux.processor.fixture.action;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/2/15.
 */
public class AddTodoItemAction extends Action {
    public static final String TAG = AddTodoItemAction.class.getSimpleName();

    private final String value;

    public AddTodoItemAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
