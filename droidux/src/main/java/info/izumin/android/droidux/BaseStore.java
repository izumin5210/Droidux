package info.izumin.android.droidux;

import io.reactivex.Single;

/**
 * Created by izumin on 12/6/15.
 */
public interface BaseStore {
    Single<Action> dispatch(Action action);
}
