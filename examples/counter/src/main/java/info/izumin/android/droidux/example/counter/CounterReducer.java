package info.izumin.android.droidux.example.counter;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.example.counter.action.DecrementCountAction;
import info.izumin.android.droidux.example.counter.action.IncrementCountAction;

/**
 * Created by izumin on 12/6/15.
 */
@Reducer(Counter.class)
public class CounterReducer {
    public static final String TAG = CounterReducer.class.getSimpleName();

    @Dispatchable(IncrementCountAction.class)
    public Counter increment(Counter state) {
        return new Counter(state.getCount() + 1);
    }

    @Dispatchable(DecrementCountAction.class)
    public Counter decrement(Counter state) {
        return new Counter(state.getCount() - 1);
    }
}
