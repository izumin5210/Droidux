package info.izumin.android.droidux;

import rx.Observable;

/**
 * Created by izumin on 11/2/15.
 */
public abstract class Middleware {
    public static final String TAG = Middleware.class.getSimpleName();

    private Store store;

    public void onAttach(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public abstract Observable<Action> beforeDispatch(Action action);
    public abstract Observable<Action> afterDispatch(Action action);
}
