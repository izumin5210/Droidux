package info.izumin.android.droidux.example.todoswithdagger.module.main;

import android.support.annotation.StringRes;

/**
 * Created by izumin on 12/2/15.
 */
public interface MainView {
    void showToast(@StringRes int resId, Object... args);
    void showConfirmDeleteDialog(long id);
}
