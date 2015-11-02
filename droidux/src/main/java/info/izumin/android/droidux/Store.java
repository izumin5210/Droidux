package info.izumin.android.droidux;

import android.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class Store<T> extends BaseObservable {
    public static final String TAG = Store.class.getSimpleName();

    private final BehaviorSubject<T> subject;
    private final List<Middleware> middlewares;
    private T state;

    private Store(Builder builder) {
        subject = BehaviorSubject.create();
        middlewares = builder.middlewares;
        for (Middleware middleware : builder.middlewares) { middleware.onAttach(this); }
    }

    public Observable<T> observe() {
        return subject;
    }

    public T getState() {
        return state;
    }

    public Observable<Action> dispatch(Action action) {
        Observable<Action> o = Observable.just(action);
        ListIterator<Middleware> iterator = getMiddlewares().listIterator();
        while (iterator.hasNext()) {
            Middleware mw = iterator.next();
            o = o.map(mw::beforeDispatch);
        }

        o = o.flatMap(a -> a.call(this));
        o = o.doOnNext(this::dispatchToReducers);

        while (iterator.hasPrevious()) {
            Middleware mw = iterator.previous();
            o = o.map(mw::afterDispatch);
        }
        return o;
    }

    protected void setState(T state) {
        this.state = state;
        notifyChange();
        subject.onNext(state);
    }

    protected List<Middleware> getMiddlewares() {
        return middlewares;
    }

    protected abstract void dispatchToReducers(Action action);

    public static abstract class Builder {
        private final List<Middleware> middlewares;

        public Builder() {
            middlewares = new ArrayList<>();
        }

        public Builder apply(Middleware middleware) {
            middlewares.add(middleware);
            return this;
        }

        public abstract Store build();
    }
}