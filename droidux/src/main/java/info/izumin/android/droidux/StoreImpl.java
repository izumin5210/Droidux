package info.izumin.android.droidux;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;

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

    public Flowable<T> observe() {
        return observe(BackpressureStrategy.DROP);
    }

    public Flowable<T> observe(BackpressureStrategy strategy) {
        return subject.toFlowable(strategy);
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
