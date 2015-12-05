package info.izumin.android.droidux.action;

/**
 * Created by izumin on 11/24/15.
 */
public class UndoAction extends HistoryAction {
    public static final String TAG = UndoAction.class.getSimpleName();

    public UndoAction(Class targetReducerType) {
        super(Kind.UNDO, targetReducerType);
    }
}
