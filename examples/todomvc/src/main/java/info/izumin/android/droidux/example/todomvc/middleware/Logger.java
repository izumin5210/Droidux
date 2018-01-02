package info.izumin.android.droidux.example.todomvc.middleware;

import android.util.Log;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.example.todomvc.RootStore;
import io.reactivex.Single;
import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
public class Logger extends Middleware<RootStore> {
    public static final String TAG = Logger.class.getSimpleName();

    @Override
    public Single<Action> beforeDispatch(Action action) {
        Log.d("[prev todo]", getStore().todoList().toString());
        Log.d("[" + action.getClass().getSimpleName() + "]", action.toString());
        return Single.just(action);
    }

    @Override
    public Single<Action> afterDispatch(Action action) {
        Log.d("[next todo]", getStore().todoList().toString());
        return Single.just(action);
    }
}
