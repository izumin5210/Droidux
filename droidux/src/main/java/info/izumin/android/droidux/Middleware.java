package info.izumin.android.droidux;

import rx.Observable;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class Middleware<S> {
    public static final String TAG = Middleware.class.getSimpleName();

    private S store;

    public void onAttach(S store) {
        this.store = store;
    }

    public S getStore() {
        return store;
    }

    public abstract Observable<Action> beforeDispatch(Action action);
    public abstract Observable<Action> afterDispatch(Action action);
}
