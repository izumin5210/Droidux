package info.izumin.android.droidux;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class StoreImpl<T, R> {
    public static final String TAG = StoreImpl.class.getSimpleName();

    private final BehaviorSubject<T> subject;
    private T state;
    private final R reducer;
    private final Set<OnStateChangedListener<T>> listeners;

    protected StoreImpl(T state, R reducer) {
        this.state = state;
        this.reducer = reducer;
        subject = BehaviorSubject.create();
        listeners = new HashSet<>();
    }

    public Observable<T> observe() {
        return subject;
    }

    public T getState() {
        return state;
    }

    protected void setState(T state) {
        this.state = state;
        subject.onNext(state);
        for (OnStateChangedListener<T> listener : listeners) {
            listener.onStateChanged(state);
        }
    }

    protected R getReducer() {
        return reducer;
    }

    public void addListener(OnStateChangedListener<T> listener) {
        listeners.add(listener);
    }

    protected abstract void dispatch(Action action);
}
