package info.izumin.android.droidux.action;

import info.izumin.android.droidux.UndoableState;

/**
 * Created by izumin on 11/24/15.
 */
public class UndoAction extends HistoryAction {
    public static final String TAG = UndoAction.class.getSimpleName();

    public UndoAction(Class<? extends UndoableState> targetStateType) {
        super(Kind.UNDO, targetStateType);
    }
}
