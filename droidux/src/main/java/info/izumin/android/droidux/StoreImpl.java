package info.izumin.android.droidux;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class StoreImpl<T> extends BaseObservable {
    public static final String TAG = StoreImpl.class.getSimpleName();

    private final BehaviorSubject<T> subject;
    private T state;

    protected StoreImpl(T state) {
        this.state = state;
        subject = BehaviorSubject.create();
    }

    public Observable<T> observe() {
        return subject;
    }

    @Bindable
    public T getState() {
        return state;
    }

    protected void setState(T state) {
        this.state = state;
        notifyChange();
        subject.onNext(state);
    }

    protected abstract void dispatch(Action action);
}
