package info.izumin.android.droidux;

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

    public abstract <A extends Action, B extends Action> B beforeDispatch(A action);
    public abstract <A extends Action, B extends Action> B afterDispatch(A action);
}
