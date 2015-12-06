package info.izumin.android.droidux;

/**
 * Created by izumin on 12/6/15.
 */
public interface BaseStore extends android.databinding.Observable {
    rx.Observable<Action> dispatch(Action action);
}
