package info.izumin.android.droidux.sample.middleware;

import android.util.Log;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.sample.entity.TodoList;
import info.izumin.android.droidux.sample.reducer.DroiduxRootStore;
import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
public class Logger extends Middleware {
    public static final String TAG = Logger.class.getSimpleName();

    @Override
    public Observable<Action> beforeDispatch(Action action) {
        Log.d("[prev todo]", getTodoList().toString());
        Log.d("[" + action.getClass().getSimpleName() + "]", action.toString());
        return Observable.just(action);
    }

    @Override
    public Observable<Action> afterDispatch(Action action) {
        Log.d("[next todo]", getTodoList().toString());
        return Observable.just(action);
    }

    private TodoList getTodoList() {
        return ((DroiduxRootStore) getStore()).getTodoListStore().getState();
    }
}
