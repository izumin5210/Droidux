package info.izumin.android.droidux.thunk;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created by izumin on 11/29/15.
 */
public class ThunkMiddleware extends Middleware {
    public static final String TAG = ThunkMiddleware.class.getSimpleName();

    @Override
    public Single<Action> beforeDispatch(final Action action) {
        if (action instanceof AsyncAction) {
            return ((AsyncAction) action).call(getDispatcher())
                    .flatMap(new Function<Action, SingleSource<? extends Action>>() {
                        @Override
                        public Single<Action> apply(Action next) throws Exception {
                            return getDispatcher().dispatch(next);
                        }
                    })
                    .flatMap(new Function<Action, SingleSource<? extends Action>>() {
                        @Override
                        public Single<Action> apply(Action _next) throws Exception {
                            return Single.just(action);
                        }
                    });
        }
        return Single.just(action);
    }

    @Override
    public Single<Action> afterDispatch(Action action) {
        return Single.just(action);
    }
}
