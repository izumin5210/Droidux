package info.izumin.android.droidux.processor.fixture;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;

/**
 * Created by izumin on 11/3/15.
 */
@Reducer(Counter.class)
public class CounterReducer {
    @Dispatchable(IncrementCountAction.class)
    public Counter onIncrement(Counter state) {
        return state;
    }
}
