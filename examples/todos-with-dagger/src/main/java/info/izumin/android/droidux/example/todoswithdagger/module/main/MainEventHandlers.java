package info.izumin.android.droidux.example.todoswithdagger.module.main;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by izumin on 12/5/15.
 */
public interface MainEventHandlers {
    void onClickAddTodo(View view);
    void onItemClick(AdapterView<?> parent, View view, int position, long id);
    boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);
    void onNewTodoTextChanged(CharSequence s, int start, int before, int count);
}
