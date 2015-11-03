package info.izumin.android.droidux.sample;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import info.izumin.android.droidux.sample.action.AddTodoAction;
import info.izumin.android.droidux.sample.databinding.ActivityMainBinding;
import info.izumin.android.droidux.sample.entity.TodoList;
import info.izumin.android.droidux.sample.middleware.Logger;
import info.izumin.android.droidux.sample.reducer.DroiduxRootStore;
import info.izumin.android.droidux.sample.reducer.TodoListReducer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText editNewTodo;
    private Button btnAddTodo;

    private DroiduxRootStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        store = new DroiduxRootStore.Builder()
                .addMiddleware(new Logger())
                .addReducer(new TodoListReducer())
                .addInitialState(new TodoList(new ArrayList<>()))
                .build();

        binding.setTasks(store.getTodoListStore().getState());
        editNewTodo = (EditText) findViewById(R.id.edit_new_todo);
        btnAddTodo = (Button) findViewById(R.id.btn_add_todo);

        PublishSubject<String> subject = PublishSubject.create();
        btnAddTodo.setOnClickListener(v -> subject.onNext(editNewTodo.getText().toString()));
        subject.filter(s -> !s.isEmpty())
                .flatMap(s -> store.dispatch(new AddTodoAction(s)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        action -> {
                            editNewTodo.setText("");
                            Log.d(TAG, String.valueOf(store.getTodoListStore().getState().getTodoList().size()));
                            Toast.makeText(MainActivity.this, R.string.toast_add_todo, Toast.LENGTH_LONG).show();
                        },
                        throwable -> {
                            Log.d(TAG, throwable.getMessage());
                        }
                );
    }

    @BindingAdapter("items")
    public static void setItem(ListView listView, TodoList tasks) {
        listView.setAdapter(new TodoListAdapter(listView.getContext(), tasks));
    }
}
