package info.izumin.android.droidux.thunk;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by izumin on 11/29/15.
 */
public class ThunkMiddleware extends Middleware {
    public static final String TAG = ThunkMiddleware.class.getSimpleName();

    @Override
    public Observable<Action> beforeDispatch(final Action action) {
        if (action instanceof AsyncAction) {
            return ((AsyncAction) action).call()
                    .flatMap(new Func1<Action, Observable<Action>>() {
                        @Override
                        public Observable<Action> call(Action next) {
                            return getDispatcher().dispatch(next);
                        }
                    })
                    .flatMap(new Func1<Action, Observable<Action>>() {
                        @Override
                        public Observable<Action> call(Action _next) {
                            return Observable.just(action);
                        }
                    });
        }
        return Observable.just(action);
    }

    @Override
    public Observable<Action> afterDispatch(Action action) {
        return Observable.just(action);
    }
}
