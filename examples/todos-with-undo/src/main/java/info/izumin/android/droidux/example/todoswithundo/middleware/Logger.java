package info.izumin.android.droidux.example.todoswithundo.middleware;

import android.util.Log;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.example.todoswithundo.RootStore;
import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
public class Logger extends Middleware<RootStore> {
    public static final String TAG = Logger.class.getSimpleName();

    @Override
    public Observable<Action> beforeDispatch(Action action) {
        Log.d("[prev todo]", getStore().todoList().toString());
        Log.d("[" + action.getClass().getSimpleName() + "]", action.toString());
        return Observable.just(action);
    }

    @Override
    public Observable<Action> afterDispatch(Action action) {
        Log.d("[next todo]", getStore().todoList().toString());
        return Observable.just(action);
    }
}
