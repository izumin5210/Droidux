package info.izumin.android.droidux;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by izumin on 11/23/15.
 */
public class History<T extends UndoableState<T>> {
    public static final String TAG = History.class.getSimpleName();

    private static final int DEFAULT_LIMIT = 100;

    private final Deque<T> past;
    private final Deque<T> future;
    private T present;

    private int limit = DEFAULT_LIMIT;

    public History(T initialState) {
        past = new ArrayDeque<>();
        future = new ArrayDeque<>();
        present = initialState;
    }

    public T getPresent() {
        return present;
    }

    public void insert(T state) {
        past.addFirst(present);
        if (past.size() > limit) {
            past.removeLast();
        }
        future.clear();
        present = state;
    }

    public T undo() {
        if (isUndoable()) {
            future.addFirst(present);
            present = past.removeFirst();
        }
        return present;
    }

    public T redo() {
        if (isRedoable()) {
            past.addFirst(present);
            present = future.removeFirst();
        }
        return present;
    }

    public boolean isUndoable() {
        return past.size() > 0;
    }

    public boolean isRedoable() {
        return future.size() > 0;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        while (past.size() > limit) { past.removeLast(); }
        while (future.size() > limit) { future.removeLast(); }
    }
}
