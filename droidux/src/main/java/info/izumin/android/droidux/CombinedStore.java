package info.izumin.android.droidux;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class CombinedStore extends Store {
    public static final String TAG = CombinedStore.class.getSimpleName();

    private final List<Store> stores;

    protected CombinedStore(Builder builder) {
        super(builder);
        stores = new ArrayList<>();
    }

    protected boolean addStore(Store store) {
        return stores.add(store);
    }

    @Override
    public Observable<Action> dispatch(Action action) {
        Observable<Action> observable = Observable.just(action)
                .flatMap(new Func1<Action, Observable<Action>>() {
                    @Override
                    public Observable<Action> call(Action a) {
                        return applyMiddlewaresBeforeDispatch(a);
                    }
                });

        for (final Store store : stores) {
            observable = observable
                    .flatMap(new Func1<Action, Observable<Action>>() {
                        @Override
                        public Observable<Action> call(Action action) {
                            return store.dispatch(action);
                        }
                    });
        }

        return observable
                .flatMap(new Func1<Action, Observable<Action>>() {
                    @Override
                    public Observable<Action> call(Action a) {
                        return applyMiddlewaresAfterDispatch(a);
                    }
                });
    }

    @Override
    protected void dispatchToReducer(Action action) {
    }
}
