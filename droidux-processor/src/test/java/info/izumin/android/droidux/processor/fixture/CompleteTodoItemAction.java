package info.izumin.android.droidux.processor.fixture;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/2/15.
 */
public class CompleteTodoItemAction extends Action<String> {
    public static final String TAG = CompleteTodoItemAction.class.getSimpleName();

    public CompleteTodoItemAction(String value) {
        super(value);
    }
}
