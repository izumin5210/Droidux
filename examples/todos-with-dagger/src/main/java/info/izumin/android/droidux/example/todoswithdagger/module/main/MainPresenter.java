package info.izumin.android.droidux.example.todoswithdagger.module.main;

import info.izumin.android.droidux.example.todoswithdagger.R;
import info.izumin.android.droidux.example.todoswithdagger.RootStore;
import info.izumin.android.droidux.example.todoswithdagger.action.AddTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ClearCompletedTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ClearNewTodoTextAction;
import info.izumin.android.droidux.example.todoswithdagger.action.DeleteTodoAction;
import info.izumin.android.droidux.example.todoswithdagger.action.ToggleCompletedTodoAction;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by izumin on 11/5/15.
 */
public class MainPresenter {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private final MainView view;
    private final RootStore store;

    private final PublishSubject<String> clickAddTodoSubject = PublishSubject.create();
    private final PublishSubject<Long> clickItemSubject = PublishSubject.create();
    private final PublishSubject<Long> longClickItemSubject = PublishSubject.create();

    private CompositeDisposable compositeDisposable;

    public MainPresenter(MainView view, RootStore store) {
        this.view = view;
        this.store = store;
    }

    void onStart() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(clickAddTodoSubject
                .filter(s -> !s.isEmpty())
                .flatMap(s -> store.dispatch(new AddTodoAction(s)).toObservable())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(_a -> store.dispatch(new ClearNewTodoTextAction()).toObservable())
                .subscribe(_a -> {
                    view.clearNewTodoText();
                    view.showToast(R.string.toast_add_todo);
                }));

        compositeDisposable.add(clickItemSubject
                .flatMap(id -> store.dispatch(new ToggleCompletedTodoAction(id.intValue())).toObservable())
                .subscribe());

        compositeDisposable.add(longClickItemSubject
                .flatMap(id -> store.dispatch(new DeleteTodoAction(id)).toObservable())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> view.showToast(R.string.toast_delete_todo)));
    }

    void onStop() {
        compositeDisposable.clear();
    }

    void onClickBtnAddTodo(String text) {
        clickAddTodoSubject.onNext(text);
    }

    void onClickListItem(long id) {
        clickItemSubject.onNext(id);
    }

    void onLongClickListItem(long id) {
        longClickItemSubject.onNext(id);
    }

    void clearCompletedTodo() {
        store.dispatch(new ClearCompletedTodoAction()).subscribe();
    }
}
