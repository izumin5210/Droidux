package info.izumin.android.droidux.example.todoswithdagger.util;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import info.izumin.android.droidux.example.todoswithdagger.adapter.TodoListAdapter;

/**
 * Created by izumin on 12/5/15.
 */
public final class ViewBindingUtils {
    private ViewBindingUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    @BindingAdapter("todoAdapter")
    public static void setTodoAdapter(ListView listView, TodoListAdapter adapter) {
        listView.setAdapter(adapter);
    }
}