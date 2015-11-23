package info.izumin.android.droidux.example.todoswithundo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import info.izumin.android.droidux.example.todoswithundo.databinding.ListItemTodoBinding;
import info.izumin.android.droidux.example.todoswithundo.entity.TodoList;
import info.izumin.android.droidux.example.todoswithundo.reducer.DroiduxRootStore;

/**
 * Created by izumin on 11/4/15.
 */
public class TodoListAdapter extends BaseAdapter {
    public static final String TAG = TodoListAdapter.class.getSimpleName();

    private static final int LAYOUT_RES_ID = R.layout.list_item_todo;

    private final LayoutInflater inflater;
    private final DroiduxRootStore store;

    public TodoListAdapter(Context context) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.store = ((App) context.getApplicationContext()).getStore();
        this.store.getTodoListStore().observe().subscribe(todoList -> notifyDataSetChanged());
    }

    @Override
    public int getCount() {
        return store.getTodoListStore().getState().size();
    }

    @Override
    public TodoList.Todo getItem(int position) {
        return store.getTodoListStore().getState().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemTodoBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, LAYOUT_RES_ID, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ListItemTodoBinding) convertView.getTag();
        }

        binding.setTodo(getItem(position));

        return convertView;
    }
}
