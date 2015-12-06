package info.izumin.android.droidux.action;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.History;
import info.izumin.android.droidux.UndoableState;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.annotation.Undoable;

/**
 * Created by izumin on 11/24/15.
 */
public class HistoryAction implements Action {
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
    private final Class targetReducerType;

    public HistoryAction(Kind kind, Class targetReducerType) {
        for (Class<? extends Annotation> annotationType : getNecessaryAnnotationTypes()) {
            if (targetReducerType.getAnnotation(annotationType) == null) {
                throw new IllegalArgumentException();
            }
        }
        this.kind = kind;
        this.targetReducerType = targetReducerType;
    }

    public <R> boolean isAssignableTo(R reducer) {
        return targetReducerType.equals(reducer.getClass());
    }

    public <T extends UndoableState<T>> T handle(History<T> history) {
        return kind.handle(history);
    }

    protected Set<Class<? extends Annotation>> getNecessaryAnnotationTypes() {
        return new HashSet<Class<? extends Annotation>>() {{
            add(Reducer.class);
            add(Undoable.class);
        }};
    }
}
