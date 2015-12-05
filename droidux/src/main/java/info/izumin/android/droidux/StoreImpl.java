package info.izumin.android.droidux;

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

    protected StoreImpl(T state, R reducer) {
        this.state = state;
        this.reducer = reducer;
        subject = BehaviorSubject.create();
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
    }

    protected R getReducer() {
        return reducer;
    }

    protected abstract void dispatch(Action action);
}
