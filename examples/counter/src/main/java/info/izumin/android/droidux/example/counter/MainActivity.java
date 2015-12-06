package info.izumin.android.droidux.example.counter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import info.izumin.android.droidux.example.counter.action.DecrementCountAction;
import info.izumin.android.droidux.example.counter.action.IncrementCountAction;
import info.izumin.android.droidux.example.counter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainEventHandlers {

    private RootStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        store = DroiduxRootStore.builder()
                .setReducer(new CounterReducer(), new Counter(0), BR.counter)
                .build();

        binding.setHandlers(this);
        binding.setStore(store);
    }

    @Override
    public void onClickBtnIncrement(View v) {
        store.dispatch(new IncrementCountAction()).subscribe();
    }

    @Override
    public void onClickBtnDecrement(View v) {
        store.dispatch(new DecrementCountAction()).subscribe();
    }
}
