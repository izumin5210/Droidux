package info.izumin.android.droidux;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by izumin on 12/6/15.
 */
public interface BaseStore {
    Flowable<Action> dispatch(Action action);
}
