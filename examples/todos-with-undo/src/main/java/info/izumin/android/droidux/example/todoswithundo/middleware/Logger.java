package info.izumin.android.droidux.example.todoswithundo.middleware;

import android.util.Log;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.example.todoswithundo.RootStore;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by izumin on 11/4/15.
 */
public class Logger extends Middleware<RootStore> {
    public static final String TAG = Logger.class.getSimpleName();


    @Override
    public Flowable<Action> beforeDispatch(Action action) {
        Log.d("[prev todo]", getStore().todoList().toString());
        Log.d("[" + action.getClass().getSimpleName() + "]", action.toString());
        return Flowable.just(action);
    }

    @Override
    public Flowable<Action> afterDispatch(Action action) {
        Log.d("[next todo]", getStore().todoList().toString());
        return Flowable.just(action);
    }
}
