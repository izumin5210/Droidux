package info.izumin.android.droidux;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by izumin on 11/28/15.
 */
public class Dispatcher {
    public static final String TAG = Dispatcher.class.getSimpleName();

    private final List<Middleware> middlewares;
    private final List<StoreImpl> storeImpls;

    public Dispatcher(List<Middleware> middlewares, StoreImpl... storeImpls) {
        this.middlewares = middlewares;
        this.storeImpls = Arrays.asList(storeImpls);
    }

    public Observable<Action> dispatch(Action action) {
        return Observable.just(action)
                .flatMap(new Func1<Action, Observable<Action>>() {
                    @Override
                    public Observable<Action> call(Action action) {
                        return applyMiddlewaresBeforeDispatch(action);
                    }
                })
                .doOnNext(new Action1<Action>() {
                    @Override
                    public void call(Action action) {
                        for (StoreImpl store : storeImpls) {
                            store.dispatch(action);
                        }
                    }
                })
                .flatMap(new Func1<Action, Observable<Action>>() {
                    @Override
                    public Observable<Action> call(Action action) {
                        return applyMiddlewaresAfterDispatch(action);
                    }
                });
    }

    private Observable<Action> applyMiddlewaresBeforeDispatch(Action action) {
        Observable<Action> o = Observable.just(action);

        for (final Middleware<?> mw : middlewares) {
            o = o.flatMap(new Func1<Action, Observable<Action>>() {
                @Override
                public Observable<Action> call(Action a) {
                    return mw.afterDispatch(a);
                }
            });
        }
        return o;
    }

    private Observable<Action> applyMiddlewaresAfterDispatch(Action action) {
        Observable<Action> o = Observable.just(action);
        ListIterator<Middleware> iterator = middlewares.listIterator(middlewares.size());
        while(iterator.hasPrevious()) {
            final Middleware<?> mw = iterator.previous();
            o = o.flatMap(new Func1<Action, Observable<Action>>() {
                @Override
                public Observable<Action> call(Action a) {
                    return mw.afterDispatch(a);
                }
            });
        }
        return o;
    }
}
