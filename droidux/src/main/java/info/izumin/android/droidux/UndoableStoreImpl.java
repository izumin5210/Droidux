package info.izumin.android.droidux;

import android.databinding.Bindable;

import info.izumin.android.droidux.action.HistoryAction;

/**
 * Created by izumin on 11/25/15.
 */
public abstract class UndoableStoreImpl<T extends UndoableState<T>, R> extends StoreImpl<T, R> {
    public static final String TAG = UndoableStoreImpl.class.getSimpleName();

    private final History<T> history;

    protected UndoableStoreImpl(T state, R reducer) {
        super(state, reducer);
        history = new History<>(state);
    }

    @Override
    protected void dispatch(Action action) {
        if (HistoryAction.class.isAssignableFrom(action.getClass())) {
            HistoryAction historyAction = (HistoryAction) action;
            if (((HistoryAction) action).isAssignableTo(getReducer())) {
                setStateWithoutKeepingHistory(historyAction.handle(history));
            }
        }
    }

    protected void setStateWithoutKeepingHistory(T state) {
        super.setState(state);
    }

    @Override
    protected void setState(T state) {
        history.insert(state);
        super.setState(state);
    }

    @Override
    @Bindable
    public T getState() {
        return history.getPresent();
    }

    public History<T> getHistory() {
        return history;
    }

    @Bindable
    public boolean isUndoable() {
        return history.isUndoable();
    }

    @Bindable
    public boolean isRedoable() {
        return history.isRedoable();
    }

    public void setLimit(int limit) {
        history.setLimit(limit);
    }
}
