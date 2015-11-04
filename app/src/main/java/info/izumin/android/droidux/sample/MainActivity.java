package info.izumin.android.droidux.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import info.izumin.android.droidux.sample.action.AddTodoAction;
import info.izumin.android.droidux.sample.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.sample.databinding.ActivityMainBinding;
import info.izumin.android.droidux.sample.reducer.DroiduxRootStore;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText editNewTodo;
    private Button btnAddTodo;
    private ListView listTodo;

    private DroiduxRootStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        store = ((App) getApplication()).getStore();

        editNewTodo = (EditText) findViewById(R.id.edit_new_todo);
        btnAddTodo = (Button) findViewById(R.id.btn_add_todo);
        listTodo = (ListView) findViewById(R.id.list_todo);

        PublishSubject<String> subject = PublishSubject.create();
        btnAddTodo.setOnClickListener(v -> subject.onNext(editNewTodo.getText().toString()));
        subject.filter(s -> !s.isEmpty())
                .flatMap(s -> store.dispatch(new AddTodoAction(s)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        action -> {
                            editNewTodo.setText("");
                            Toast.makeText(MainActivity.this, R.string.toast_add_todo, Toast.LENGTH_LONG).show();
                        }
                );
        listTodo.setAdapter(new TodoListAdapter(this));
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
                store.dispatch(new ClearCompletedTodoAction()).subscribe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
