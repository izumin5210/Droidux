package info.izumin.android.droidux;

import rx.Observable;

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

    public abstract Observable<Action> beforeDispatch(Action action);
    public abstract Observable<Action> afterDispatch(Action action);
}
