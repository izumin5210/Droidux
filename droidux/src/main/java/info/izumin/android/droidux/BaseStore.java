package info.izumin.android.droidux;

/**
 * Created by izumin on 12/6/15.
 */
public interface BaseStore {
    rx.Observable<Action> dispatch(Action action);
}
