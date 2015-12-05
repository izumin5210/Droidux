package info.izumin.android.droidux.action;

/**
 * Created by izumin on 11/24/15.
 */
public class RedoAction extends HistoryAction {
    public static final String TAG = RedoAction.class.getSimpleName();

    public RedoAction(Class targetReducerType) {
        super(Kind.REDO, targetReducerType);
    }
}
