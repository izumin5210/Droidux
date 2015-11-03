package info.izumin.android.droidux.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import info.izumin.android.droidux.sample.entity.TodoList;

/**
 * Created by izumin on 11/4/15.
 */
public class TodoListAdapter extends ArrayAdapter<TodoList.Todo> {
    public static final String TAG = TodoListAdapter.class.getSimpleName();

    private static final int LAYOUT_RES_ID = R.layout.list_item_todo;

    private final LayoutInflater inflater;

    public TodoListAdapter(Context context, TodoList list) {
        super(context, LAYOUT_RES_ID, list.getTodoList());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(LAYOUT_RES_ID, parent, false);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text_todo);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox_todo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TodoList.Todo todo = getItem(position);
        holder.text.setText(todo.getText());
        holder.checkbox.setChecked(todo.isCompleted());

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        CheckBox checkbox;
    }
}
