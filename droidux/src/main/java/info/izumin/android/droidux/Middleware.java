package info.izumin.android.droidux;

import io.reactivex.Single;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class Middleware<S extends BaseStore> {
    public static final String TAG = Middleware.class.getSimpleName();

    private S store;
    private Dispatcher dispatcher;

    public void onAttach(S store, Dispatcher dispatcher) {
        this.store = store;
        this.dispatcher = dispatcher;
    }

    protected S getStore() {
        return store;
    }

    protected Dispatcher getDispatcher() {
        return dispatcher;
    }

    public abstract Single<Action> beforeDispatch(Action action);
    public abstract Single<Action> afterDispatch(Action action);
}
