package info.izumin.android.droidux.sample.entity;

import java.util.List;

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

    public static class Todo {
        private int id;
        private boolean completed;
        private String text;

        public Todo(int id, String text) {
            this.id = id;
            this.text = text;
            this.completed = false;
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
    }
}
