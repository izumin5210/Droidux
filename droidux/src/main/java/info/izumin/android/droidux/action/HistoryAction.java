package info.izumin.android.droidux.action;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.UndoableState;
import info.izumin.android.droidux.History;
import info.izumin.android.droidux.Store;

/**
 * Created by izumin on 11/24/15.
 */
public class HistoryAction extends Action {
    public static final String TAG = HistoryAction.class.getSimpleName();

    enum Kind {
        UNDO {
            @Override
            <T extends UndoableState<T>> T handle(History<T> history) {
                return history.undo();
            }
        },
        REDO {
            @Override
            <T extends UndoableState<T>> T handle(History<T> history) {
                return history.redo();
            }
        };

        abstract <T extends UndoableState<T>> T handle(History<T> history);
    }

    private final Kind kind;
    private final Class<? extends Store> target;

    public HistoryAction(Kind kind, Class<? extends Store> target) {
        this.kind = kind;
        this.target = target;
    }

    public boolean isAssignableTo(Store store) {
        return target.isAssignableFrom(store.getClass());
    }

    public <T extends UndoableState<T>> T handle(History<T> history) {
        return kind.handle(history);
    }
}
