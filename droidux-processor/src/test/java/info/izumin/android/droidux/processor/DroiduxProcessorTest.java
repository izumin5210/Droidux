package info.izumin.android.droidux.processor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by izumin on 11/2/15.
 */
public class DroiduxProcessorTest {
    public static final String TAG = DroiduxProcessorTest.class.getSimpleName();

    @Test
    public void oneActionDispatchableReducer() {
        assert_().about(javaSource())
                .that(JavaFileObjects.forSourceLines("OneActionExampleReducer",
                        "package info.izumin.android.droidux.sample;",
                        "import info.izumin.android.droidux.annotation.Dispatchable;",
                        "import info.izumin.android.droidux.annotation.Reducer;",
                        "import info.izumin.android.droidux.processor.fixture.AddTodoItemAction;",
                        "import info.izumin.android.droidux.processor.fixture.TodoList;",
                        "@Reducer(TodoList.class)",
                        "public class OneActionExampleReducer {",
                        "    @Dispatchable(AddTodoItemAction.class)",
                        "    public TodoList onAddItem(TodoList state) {",
                        "        return state;",
                        "    }",
                        "}"
                ))
                .processedWith(new DroiduxProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forSourceLines("DroiduxOneActionExampleStore",
                        "package info.izumin.android.droidux.sample;",
                        "",
                        "import info.izumin.android.droidux.Action;",
                        "import info.izumin.android.droidux.Store;",
                        "import info.izumin.android.droidux.processor.fixture.AddTodoItemAction;",
                        "import info.izumin.android.droidux.processor.fixture.TodoList;",
                        "",
                        "public final class DroiduxOneActionExampleStore extends Store<TodoList> {",
                        "    private final OneActionExampleReducer oneActionExampleReducer;",
                        "",
                        "    protected DroiduxOneActionExampleStore(Builder builder) {",
                        "        super(builder);",
                        "        this.oneActionExampleReducer = builder.oneActionExampleReducer;",
                        "    }",
                        "",
                        "    @Override",
                        "    protected void dispatchToReducers(Action action) {",
                        "        Class<? extends Action> actionClass = action.getClass();",
                        "        TodoList result = null;",
                        "        if (actionClass.isAssignableFrom(AddTodoItemAction.class)) {",
                        "            result = oneActionExampleReducer.onAddItem(getState());",
                        "        }",
                        "        if (result != null) {",
                        "            setState(result);",
                        "        }",
                        "    }",
                        "",
                        "    public static class Builder extends Store.Builder {",
                        "        private OneActionExampleReducer oneActionExampleReducer;",
                        "",
                        "        public Builder() {",
                        "            super();",
                        "        }",
                        "",
                        "        public Builder addReducer(OneActionExampleReducer oneActionExampleReducer) {",
                        "            this.oneActionExampleReducer = oneActionExampleReducer;",
                        "            return this;",
                        "        }",
                        "",
                        "        @Override",
                        "        public DroiduxOneActionExampleStore build() {",
                        "            return new DroiduxOneActionExampleStore(this);",
                        "        }",
                        "    }",
                        "}"
                ));
    }
}
