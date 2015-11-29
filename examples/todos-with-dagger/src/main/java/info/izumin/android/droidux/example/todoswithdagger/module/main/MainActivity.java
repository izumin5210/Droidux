package info.izumin.android.droidux.example.todoswithdagger.module.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import info.izumin.android.droidux.example.todoswithdagger.App;
import info.izumin.android.droidux.example.todoswithdagger.R;
import info.izumin.android.droidux.example.todoswithdagger.RootStore;

public class MainActivity extends AppCompatActivity {

    @Inject RootStore store;
    @Inject MainActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupComponent();
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

    private void setupComponent() {
        ((App) getApplication()).getComponent()
                .createMainActivityComponent(new MainActivityModule(this))
                .inject(this);
    }
}
