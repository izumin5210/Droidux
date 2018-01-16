package info.izumin.android.droidux.thunk;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by izumin on 11/29/15.
 */
public class ThunkMiddleware extends Middleware {
    public static final String TAG = ThunkMiddleware.class.getSimpleName();

    @Override
    public Flowable<Action> beforeDispatch(final Action action) {
        if (action instanceof AsyncAction) {
            return ((AsyncAction) action).call(getDispatcher())
                    .flatMap(new Function<Action, Flowable<? extends Action>>() {
                        @Override
                        public Flowable<Action> apply(Action next) throws Exception {
                            return getDispatcher().dispatch(next);
                        }
                    })
                    .flatMap(new Function<Action, Flowable<? extends Action>>() {
                        @Override
                        public Flowable<Action> apply(Action _next) throws Exception {
                            return Flowable.just(action);
                        }
                    });
        }
        return Flowable.just(action);
    }

    @Override
    public Flowable<Action> afterDispatch(Action action) {
        return Flowable.just(action);
    }
}
