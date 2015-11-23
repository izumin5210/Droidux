package info.izumin.android.droidux.example.todoswithundo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import info.izumin.android.droidux.example.todoswithundo.reducer.DroiduxRootStore;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private DroiduxRootStore store;
    private MainActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        store = ((App) getApplication()).getStore();
        helper = new MainActivityHelper(this);
        helper.onCreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return helper.onOptionItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
