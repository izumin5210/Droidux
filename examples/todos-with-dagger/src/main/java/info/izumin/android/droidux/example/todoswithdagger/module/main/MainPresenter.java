package info.izumin.android.droidux.example.todoswithdagger.module.main;

import info.izumin.android.droidux.example.todoswithdagger.R;
import info.izumin.android.droidux.example.todoswithdagger.RootStore;
import info.izumin.android.droidux.example.todoswithdagger.action.AddTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ClearNewTodoTextAction;
import info.izumin.android.droidux.example.todoswithdagger.action.DeleteTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ToggleCompletedTodoAction;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.SingleSubject;

/**
 * Created by izumin on 11/5/15.
 */
public class MainPresenter {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private final MainView view;
    private final RootStore store;

    private final SingleSubject<String> clickAddTodoSubject = SingleSubject.create();
    private final SingleSubject<Long> clickItemSubject = SingleSubject.create();
    private final SingleSubject<Long> longClickItemSubject = SingleSubject.create();

    private CompositeDisposable compositeDisposable;

    public MainPresenter(MainView view, RootStore store) {
        this.view = view;
        this.store = store;
    }

    void onStart() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(clickAddTodoSubject
                .filter(s -> !s.isEmpty())
                .flatMap(s -> store.dispatch(new AddTodoAction(s)).toMaybe())
                /*TODO: Version up RxAndroid*/
                /*.subscribeOn(AndroidSchedulers.mainThread())*/
                .flatMap(_a -> store.dispatch(new ClearNewTodoTextAction()).toMaybe())
                .subscribe(_a -> {
                    view.clearNewTodoText();
                    view.showToast(R.string.toast_add_todo);
                }));

        compositeDisposable.add(clickItemSubject
                .flatMap(id -> store.dispatch(new ToggleCompletedTodoAction(id.intValue())))
                .subscribe());

        compositeDisposable.add(longClickItemSubject
                .flatMap(id -> store.dispatch(new DeleteTodoAction(id)))
                /*TODO: Version up RxAndroid*/
                /*.subscribeOn(AndroidSchedulers.mainThread())*/
                .subscribe(action -> view.showToast(R.string.toast_delete_todo)));
    }

    void onStop() {
        compositeDisposable.clear();
    }

    void onClickBtnAddTodo(String text) {
        clickAddTodoSubject.onSuccess(text);
    }

    void onClickListItem(long id) {
        clickItemSubject.onSuccess(id);
    }

    void onLongClickListItem(long id) {
        longClickItemSubject.onSuccess(id);
    }

    void clearCompletedTodo() {
        store.dispatch(new ClearCompletedTodoAction()).subscribe();
    }
}
