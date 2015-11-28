package info.izumin.android.droidux.processor.fixture;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.processor.fixture.action.ClearCountAction;
import info.izumin.android.droidux.processor.fixture.action.IncrementCountAction;
import info.izumin.android.droidux.processor.fixture.action.InitializeCountAction;
import info.izumin.android.droidux.processor.fixture.action.SquareCountAction;

/**
 * Created by izumin on 11/3/15.
 */
@Reducer(Counter.class)
public class CounterReducer {
    @Dispatchable(IncrementCountAction.class)
    public Counter icrement(Counter state, IncrementCountAction action) {
        return new Counter(state.getCount() + action.getValue());
    }

    @Dispatchable(SquareCountAction.class)
    public Counter square(Counter state) {
        return new Counter(state.getCount() * state.getCount());
    }

    @Dispatchable(InitializeCountAction.class)
    public Counter initialize(InitializeCountAction action) {
        return new Counter(action.getValue());
    }

    @Dispatchable(ClearCountAction.class)
    public Counter clear() {
        return new Counter(0);
    }
}
