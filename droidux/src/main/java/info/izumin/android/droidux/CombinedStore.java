package info.izumin.android.droidux;

import java.util.ArrayList;
import java.util.List;

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
    protected void dispatchToReducer(Action action) {
        for (Store store : stores) {
            store.dispatch(action);
        }
    }
}
