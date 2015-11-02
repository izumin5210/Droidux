package info.izumin.android.droidux.processor.fixture;

import info.izumin.android.droidux.Action;

/**
 * Created by izumin on 11/2/15.
 */
public class IncrementCountAction extends Action<Integer> {
    public static final String TAG = IncrementCountAction.class.getSimpleName();

    public IncrementCountAction() {
        super(1);
    }
}
