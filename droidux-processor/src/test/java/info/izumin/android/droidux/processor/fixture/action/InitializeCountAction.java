package info.izumin.android.droidux.processor.fixture.action;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/2/15.
 */
public class InitializeCountAction implements Action {
    public static final String TAG = InitializeCountAction.class.getSimpleName();

    private final int value;

    public InitializeCountAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
