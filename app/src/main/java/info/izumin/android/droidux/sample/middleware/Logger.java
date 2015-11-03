package info.izumin.android.droidux.sample.middleware;

import android.util.Log;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.sample.reducer.DroiduxRootStore;
import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
public class Logger extends Middleware {
    public static final String TAG = Logger.class.getSimpleName();

    @Override
    public Observable<Action> beforeDispatch(Action action) {
        Log.d("[prev todo count]", String.valueOf(getTodoCount()));
        Log.d("[action]", action.getClass().getSimpleName());
        return Observable.just(action);
    }

    @Override
    public Observable<Action> afterDispatch(Action action) {
        Log.d("[next todo count]", String.valueOf(getTodoCount()));
        return Observable.just(action);
    }

    private int getTodoCount() {
        return ((DroiduxRootStore) getStore()).getTodoListStore().getState().getTodoList().size();
    }
}
