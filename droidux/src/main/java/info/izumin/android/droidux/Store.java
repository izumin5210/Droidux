package info.izumin.android.droidux;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class Store<T> extends BaseObservable {
    public static final String TAG = Store.class.getSimpleName();

    private final BehaviorSubject<T> subject;
    private final List<Middleware> middlewares;
    private T state;

    protected Store(Builder builder) {
        subject = BehaviorSubject.create();
        middlewares = builder.middlewares;
        for (Middleware middleware : builder.middlewares) { middleware.onAttach(this); }
    }

    public Observable<T> observe() {
        return subject;
    }

    @Bindable
    public T getState() {
        return state;
    }

    public Observable<Action> dispatch(Action action) {
        Observable<Action> o = Observable.just(action);
        final ListIterator<Middleware> iterator = getMiddlewares().listIterator();
        while (iterator.hasNext()) {
            final Middleware mw = iterator.next();
            o = o.map(new Func1<Action, Action>() {
                @Override
                public Action call(Action action) {
                    return mw.beforeDispatch(action);
                }
            });
        }

        o = o.flatMap(new Func1<Action, Observable<Action>>() {
            @Override
            public Observable<Action> call(Action a) {
                return a.call(Store.this);
            }
        }).doOnNext(new Action1<Action>() {
            @Override
            public void call(Action a) {
                dispatchToReducer(a);
            }
        });

        while (iterator.hasPrevious()) {
           final Middleware mw = iterator.previous();
            o = o.map(new Func1<Action, Action>() {
                @Override
                public Action call(Action a) {
                    return mw.afterDispatch(a);
                }
            });
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

    protected abstract void dispatchToReducer(Action action);

    public static abstract class Builder {
        private final List<Middleware> middlewares;

        public Builder() {
            this.middlewares = new ArrayList<>();
        }

        protected List<Middleware> getMiddlewares() {
            return middlewares;
        }

        public abstract Store build();
    }
}
