package info.izumin.android.droidux.example.todoswithundo.reducer;

import info.izumin.android.droidux.annotation.CombinedReducer;

/**
 * Created by izumin on 11/4/15.
 */
@CombinedReducer(TodoListReducer.class)
public class RootReducer {
    public static final String TAG = RootReducer.class.getSimpleName();
}
