package info.izumin.android.droidux.processor.fixture.action;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/2/15.
 */
public class IncrementCountAction implements Action {
    public static final String TAG = IncrementCountAction.class.getSimpleName();

    private final int value;

    public IncrementCountAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
