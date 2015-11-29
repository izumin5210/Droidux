package info.izumin.android.droidux.processor.fixture;

import java.util.ArrayList;

import info.izumin.android.droidux.UndoableState;

/**
 * Created by izumin on 11/2/15.
 */
public class TodoList extends ArrayList<TodoList.Item> implements UndoableState<TodoList> {

    @Override
    public TodoList clone() {
        return (TodoList) super.clone();
    }

    public static class Item {
        private String body;
        private boolean isCompleted;

        public Item(String body) {
            this.body = body;
            this.isCompleted = false;
        }

        public String getBody() {
            return body;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean isCompleted) {
            this.isCompleted = isCompleted;
        }
    }
}
