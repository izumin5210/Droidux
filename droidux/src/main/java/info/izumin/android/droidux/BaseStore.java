package info.izumin.android.droidux;

import rx.Observable;

/**
 * Created by izumin on 12/6/15.
 */
public interface BaseStore {
    Observable<Action> dispatch(Action action);
}
