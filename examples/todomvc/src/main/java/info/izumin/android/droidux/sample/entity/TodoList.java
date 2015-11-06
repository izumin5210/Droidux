package info.izumin.android.droidux.sample.entity;

import com.google.gson.Gson;

import java.util.List;

import rx.Observable;

/**
 * Created by izumin on 11/4/15.
 */
public class TodoList {
    public static final String TAG = TodoList.class.getSimpleName();

    private List<Todo> todoList;

    public TodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public Todo getTodoById(int id) {
        return Observable.from(getTodoList())
                .filter(todo -> id == todo.getId()).toBlocking().first();
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
