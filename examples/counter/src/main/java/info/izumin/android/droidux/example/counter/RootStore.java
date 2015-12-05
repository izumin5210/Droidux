package info.izumin.android.droidux.example.counter;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.annotation.Store;
import rx.Observable;

/**
 * Created by izumin on 12/6/15.
 */
@Store(CounterReducer.class)
public interface RootStore {
    Counter counter();
    Observable<Action> dispatch(Action action);
}
