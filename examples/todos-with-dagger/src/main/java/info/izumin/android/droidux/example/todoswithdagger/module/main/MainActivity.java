package info.izumin.android.droidux.example.todoswithdagger.module.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import javax.inject.Inject;

import info.izumin.android.droidux.example.todoswithdagger.App;
import info.izumin.android.droidux.example.todoswithdagger.R;
import info.izumin.android.droidux.example.todoswithdagger.RootStore;
import info.izumin.android.droidux.example.todoswithdagger.adapter.TodoListAdapter;
import info.izumin.android.droidux.example.todoswithdagger.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainView, MainEventHandlers {

    @Inject MainPresenter presenter;
    @Inject RootStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupComponent();
        binding.setHandlers(this);
        binding.setTodoAdapter(new TodoListAdapter(this));
        binding.setStore(store);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_completed_todo:
                presenter.clearCompletedTodo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showToast(@StringRes int resId, Object... args) {
        Toast.makeText(this, getString(resId, args), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showConfirmDeleteDialog(long id) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_todo_title)
                .setMessage(getString(R.string.dialog_delete_todo_message,
                        store.todoList().getTodoById((int) id).getText()))
                .setPositiveButton(R.string.dialog_delete_todo_btn_positive, (dialog, which) -> {
                    presenter.onLongClickListItem(id);
                })
                .setNeutralButton(R.string.dialog_delete_todo_btn_neutral, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void onClickAddTodo(View view) {
        presenter.onClickBtnAddTodo(store.getNewTodoText());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onClickListItem(id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showConfirmDeleteDialog(id);
        return true;
    }

    @Override
    public void onNewTodoTextChanged(CharSequence s, int start, int before, int count) {
        presenter.updateNewTodoText(s.toString());
    }

    protected MainModule getModule() {
        return new MainModule(this);
    }

    private void setupComponent() {
        ((App) getApplication()).getComponent()
                .createMainActivityComponent(getModule())
                .inject(this);
    }
}
