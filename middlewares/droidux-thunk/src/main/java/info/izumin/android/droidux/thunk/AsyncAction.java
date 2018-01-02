package info.izumin.android.droidux.thunk;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Dispatcher;
import io.reactivex.Single;

/**
 * Created by izumin on 11/29/15.
 */
public interface AsyncAction extends Action {
    Single<Action> call(Dispatcher dispatcher);
}
