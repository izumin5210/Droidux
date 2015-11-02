package info.izumin.android.droidux.processor.fixture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izumin on 11/2/15.
 */
public class TodoList {

    private List<Item> items;

    public TodoList() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
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
