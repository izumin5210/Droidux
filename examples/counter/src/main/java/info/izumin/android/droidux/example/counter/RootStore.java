package info.izumin.android.droidux.example.counter;

import android.databinding.Bindable;

import info.izumin.android.droidux.BaseStore;
import info.izumin.android.droidux.annotation.Store;

/**
 * Created by izumin on 12/6/15.
 */
@Store(CounterReducer.class)
public interface RootStore extends BaseStore, android.databinding.Observable {
    @Bindable Counter getCounter();
}
