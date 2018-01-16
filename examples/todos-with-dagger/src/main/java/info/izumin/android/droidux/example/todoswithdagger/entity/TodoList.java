package info.izumin.android.droidux.example.todoswithdagger.entity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import info.izumin.android.droidux.UndoableState;
import io.reactivex.Observable;

/**
 * Created by izumin on 11/4/15.
 */
public class TodoList extends ArrayList<TodoList.Todo> implements UndoableState<TodoList> {
    public static final String TAG = TodoList.class.getSimpleName();

    public TodoList() {
    }

    public TodoList(List<Todo> list) {
        super(list);
    }

    public Todo getTodoById(int id) {
        return Observable.fromIterable(this)
                .filter(todo -> id == todo.getId()).blockingFirst();
    }

    @Override
    public TodoList clone() {
        return (TodoList) super.clone();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class Todo {
        private final int id;
        private final boolean completed;
        private final String text;

        public Todo(int id, String text) {
            this.id = id;
            this.text = text;
            this.completed = false;
        }

        public Todo(int id, String text, boolean isCompleted) {
            this.id = id;
            this.text = text;
            this.completed = isCompleted;
        }

        public int getId() {
            return id;
        }

        public boolean isCompleted() {
            return completed;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
