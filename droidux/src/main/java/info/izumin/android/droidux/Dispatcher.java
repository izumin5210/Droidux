package info.izumin.android.droidux;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

    public Flowable<Action> dispatch(Action action) {
        return Flowable.just(action)
                .flatMap(new Function<Action, Flowable<? extends Action>>() {
                    @Override
                    public Flowable<? extends Action> apply(Action action) throws Exception {
                        return applyMiddlewaresBeforeDispatch(action);
                    }
                }).doOnNext(new Consumer<Action>() {
                    @Override
                    public void accept(Action action) throws Exception {
                        for (StoreImpl store : storeImpls) {
                            store.dispatch(action);
                        }
                    }
                })
                .flatMap(new Function<Action, Flowable<? extends Action>>() {
                    @Override
                    public Flowable<? extends Action> apply(Action action) throws Exception {
                        return applyMiddlewaresAfterDispatch(action);
                    }
                });
    }

    private Flowable<Action> applyMiddlewaresBeforeDispatch(Action action) {
        Flowable<Action> o = Flowable.just(action);

        for (final Middleware<?> mw : middlewares) {
            o = o.flatMap(new Function<Action, Flowable<? extends Action>>() {
                @Override
                public Flowable<Action> apply(Action a) throws Exception {
                    return mw.beforeDispatch(a);
                }
            });
        }
        return o;
    }

    private Flowable<Action> applyMiddlewaresAfterDispatch(Action action) {
        Flowable<Action> o = Flowable.just(action);
        ListIterator<Middleware> iterator = middlewares.listIterator(middlewares.size());
        while(iterator.hasPrevious()) {
            final Middleware<?> mw = iterator.previous();
            o = o.flatMap(new Function<Action, Flowable<? extends Action>>() {
                @Override
                public Flowable<Action> apply(Action a) throws Exception {
                    return mw.afterDispatch(a);
                }
            });
        }
        return o;
    }
}
