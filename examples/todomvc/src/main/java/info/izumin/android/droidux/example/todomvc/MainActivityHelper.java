package info.izumin.android.droidux.example.todomvc;

import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import info.izumin.android.droidux.example.todomvc.action.AddTodoAction;
import info.izumin.android.droidux.example.todomvc.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.example.todomvc.action.DeleteTodoAction;
import info.izumin.android.droidux.example.todomvc.action.ToggleCompletedTodoAction;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;

/**
 * Created by izumin on 11/5/15.
 */
public class MainActivityHelper {
    public static final String TAG = MainActivityHelper.class.getSimpleName();

    private MainActivity activity;
    private RootStore store;

    private EditText editNewTodo;
    private Button btnAddTodo;
    private ListView listTodo;

    public MainActivityHelper(MainActivity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        store = ((App) activity.getApplication()).getStore();

        editNewTodo = (EditText) activity.findViewById(R.id.edit_new_todo);
        btnAddTodo = (Button) activity.findViewById(R.id.btn_add_todo);
        listTodo = (ListView) activity.findViewById(R.id.list_todo);

        observeOnClickBtnAddTodo()
                .filter(s -> !s.isEmpty())
                .flatMap(s -> store.dispatch(new AddTodoAction(s)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    editNewTodo.setText("");
                    Toast.makeText(activity, R.string.toast_add_todo, Toast.LENGTH_SHORT).show();
                });

        observeOnClickListItem()
                .flatMap(id -> store.dispatch(new ToggleCompletedTodoAction(id.intValue())))
                .subscribe();

        observeOnLongClickListItem()
                .flatMap(id -> store.dispatch(new DeleteTodoAction(id)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    Toast.makeText(activity, R.string.toast_delete_todo, Toast.LENGTH_SHORT).show();
                });

        listTodo.setAdapter(new TodoListAdapter(activity));
    }

    public boolean onOptionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_completed_todo:
                store.dispatch(new ClearCompletedTodoAction()).subscribe();
                return true;
            default:
                return false;
        }
    }

    private Flowable<String> observeOnClickBtnAddTodo() {
        PublishSubject<String> subject = PublishSubject.create();
        btnAddTodo.setOnClickListener(v -> subject.onNext(editNewTodo.getText().toString()));
        return subject.toFlowable(BackpressureStrategy.DROP);
    }

    private Flowable<Long> observeOnClickListItem() {
        PublishSubject<Long> subject = PublishSubject.create();
        listTodo.setOnItemClickListener((parent, view, position, id) -> subject.onNext(id));
        return subject.toFlowable(BackpressureStrategy.DROP);
    }

    private Flowable<Long> observeOnLongClickListItem() {
        PublishSubject<Long> subject = PublishSubject.create();
        listTodo.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.dialog_delete_todo_title)
                    .setMessage(activity.getString(R.string.dialog_delete_todo_message,
                            store.todoList().getTodoById((int) id).getText()))
                    .setPositiveButton(R.string.dialog_delete_todo_btn_positive, (dialog, which) -> {
                        subject.onNext(id);
                    })
                    .setNeutralButton(R.string.dialog_delete_todo_btn_neutral, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return true;
        });
        return subject.toFlowable(BackpressureStrategy.DROP);
    }
}
