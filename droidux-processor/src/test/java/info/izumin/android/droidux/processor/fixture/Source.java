package info.izumin.android.droidux.processor.fixture;

/**
 * Created by izumin on 11/2/15.
 */
public final class Source {
    public static final String TAG = Source.class.getSimpleName();

    public static final String[] EMPTY = {};

    public static final class StoreImpl {
        public static final String[] COUNTER = {
                "package info.izumin.android.droidux.sample;",
                "",
                "import info.izumin.android.droidux.Action;",
                "import info.izumin.android.droidux.StoreImpl;",
                "import info.izumin.android.droidux.processor.fixture.Counter;",
                "import info.izumin.android.droidux.processor.fixture.action.ClearCountAction;",
                "import info.izumin.android.droidux.processor.fixture.action.IncrementCountAction;",
                "import info.izumin.android.droidux.processor.fixture.action.InitializeCountAction;",
                "import info.izumin.android.droidux.processor.fixture.action.SquareCountAction;",
                "",
                "public final class RootStore_CounterStoreImpl extends StoreImpl<Counter> {",
                "    private final CounterReducer reducer;",
                "",
                "    protected RootStore_CounterStoreImpl(Counter state, CounterReducer reducer) {",
                "        super(state);",
                "        this.reducer = builder.reducer;",
                "    }",
                "",
                "    @Override",
                "    protected void dispatchToReducer(Action action) {",
                "        Class<? extends Action> actionClass = action.getClass();",
                "        Counter result = null;",
                "        if (IncrementCountAction.class.isAssignableFrom(actionClass)) {",
                "            result = counterReducer.increment(getState(), (IncrementCountAction) action);",
                "        }",
                "        if (SquareCountAction.class.isAssignableFrom(actionClass)) {",
                "            result = counterReducer.square(getState());",
                "        }",
                "        if (InitializeCountAction.class.isAssignableFrom(actionClass)) {",
                "            result = counterReducer.initialize((InitializeCountAction) action);",
                "        }",
                "        if (ClearCountAction.class.isAssignableFrom(actionClass)) {",
                "            result = counterReducer.clear();",
                "        }",
                "        if (result != null) {",
                "            setState(result);",
                "        }",
                "    }",
                "}"
        };

        public static final String[] TODO_LIST = {
                "package info.izumin.android.droidux.sample;",
                "",
                "import info.izumin.android.droidux.Action;",
                "import info.izumin.android.droidux.StoreImpl;",
                "import info.izumin.android.droidux.processor.fixture.TodoList;",
                "import info.izumin.android.droidux.processor.fixture.action.AddTodoItemAction;",
                "",
                "public final class RootStore_TodoListStoreImpl extends UndoableStoreImpl<TodoList> {",
                "    private final TodoListReducer reducer;",
                "",
                "    protected CounterStore_CounterStoreImpl(TodoList state, TodoListReducer reducer) {",
                "        super(state);",
                "        this.reducer = builder.reducer;",
                "    }",
                "",
                "    @Override",
                "    protected void dispatchToReducer(Action action) {",
                "        super.dispatchToReducer(action);",
                "        Class<? extends Action> actionClass = action.getClass();",
                "        Counter result = null;",
                "        if (AddTodoItemAction.class.isAssignableFrom(actionClass)) {",
                "            result = todoListReducer.add(getState(), (AddTodoItemAction) action);",
                "        }",
                "        if (result != null) {",
                "            setState(result);",
                "        }",
                "    }",
                "}"
        };
    }

    public static class Counter {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.sample;",
                "import info.izumin.android.droidux.annotation.Store;",
                "import info.izumin.android.droidux.processor.fixture.CounterReducer;",
                "@Store({CounterReducer.class})",
                "public interface RootStore {",
                "    Counter counter();",
                "    Observable<Counter> observeCounter();",
                "    Observable<Action> dispatch();",
                "}"
        };

        public static final String[] GENERATED_STORE = {
                "package info.izumin.android.droidux.sample;",
                "",
                "import info.izumin.android.droidux.Action;",
                "import info.izumin.android.droidux.StoreImpl;",
                "import info.izumin.android.droidux.processor.fixture.Counter;",
                "",
                "public final class DroiduxRootStore implements RootStore {",
                "    private final RootStore_CounterStoreImpl counterStoreImpl;",
                "    private final Dispatcher dispatcher;",
                "",
                "    protected DroiduxRootStore(Builder builder) {",
                "        counterStoreImpl= new RootStore_CounterStoreImpl(builder.counter, builder.counterReducer);",
                "        dispatcher = new Dispatcher(builder.middlewares, counterStoreImpl);",
                "    }",
                "",
                "    public Counter counter() {",
                "        return counterStoreImpl.getState();",
                "    }",
                "",
                "    public Observable<Counter> observeCounter() {",
                "        return counterStoreImpl.observe();",
                "    }",
                "",
                "    public Observable<Action> dispatch(Action action) {",
                "        return dispatcher.dispatch(action)",
                "    }",
                "",
                "    public static final class Builder {",
                "        private final List<Middleware> middlewares;",
                "        private CounterReducer counterReducer;",
                "        private Counter counter;",
                "",
                "        public Builder() {",
                "            middlewares = new ArrayList<>();",
                "        }",
                "",
                "        public Builder addMiddleware(Middleware middleware) {",
                "            middlewares.add(middleware);",
                "            return this;",
                "        }",
                "",
                "        public Builder setReducer(CounterReducer counterReducer) {",
                "            this.counterReducer = counterReducer;",
                "            return this;",
                "        }",
                "",
                "        public Builder setInitialState(Counter counter) {",
                "            this.counter = counter;",
                "            return this;",
                "        }",
                "",
                "        public DroiduxRootStore build() {",
                "            if (counterReducer == null) {",
                "                throw new NotInitializedException(\"CounterReducer has not been initialized.\");",
                "            }",
                "            if (counter == null) {",
                "                throw new NotInitializedException(\"Counter has not been initialized.\");",
                "            }",
                "            return new DroiduxCounterStore(this);",
                "        }",
                "    }",
                "}"
        };
    }

    public static class CombinedTwoReducers {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.processor.fixture;",
                "import info.izumin.android.droidux.annotation.Store;",
                "import info.izumin.android.droidux.processor.fixture.CounterReducer;",
                "import info.izumin.android.droidux.processor.fixture.TodoListReducer;",
                "@Store({CounterReducer.class, TodoListReducer.class})",
                "public interface RootStore {",
                "    Counter counter();",
                "    Observable<Counter> observeCounter();",
                "    TodoList todoList();",
                "    Observable<TodoList> observeTodoList();",
                "    Observable<Action> dispatch();",
                "}"
        };

        public static final String[] GENERATED = {
                "package info.izumin.android.droidux.processor.fixture;",
                "",
                "import info.izumin.android.droidux.CombinedStore;",
                "import info.izumin.android.droidux.Middleware;",
                "import info.izumin.android.droidux.exception.NotInitializedException;",
                "",
                "public final class DroiduxRootStore implements RootStore {",
                "    private final RootStore_CounterStoreImpl counterStoreImpl;",
                "    private final Dispatcher dispatcher;",
                "",
                "    protected DroiduxRootStore(Builder builder) {",
                "        counterStoreImpl= new RootStore_CounterStoreImpl(builder.counter, builder.counterReducer);",
                "        todoListStoreImpl= new RootStore_TodoListStoreImpl(builder.todoList, builder.todoListReducer);",
                "        dispatcher = new Dispatcher(builder.middlewares, counterStoreImpl, todoListStoreImpl);",
                "    }",
                "",
                "    public Counter counter() {",
                "        return counterStoreImpl.getState();",
                "    }",
                "",
                "    public Observable<Counter> observeCounter() {",
                "        return counterStoreImpl.observe();",
                "    }",
                "",
                "    public TodoList todoList() {",
                "        return todoListStoreImpl.getState();",
                "    }",
                "",
                "    public Observable<TodoList> observeTodoList() {",
                "        return todoListStoreImpl.observe();",
                "    }",
                "",
                "    public Observable<Action> dispatch(Action action) {",
                "        return dispatcher.dispatch(action)",
                "    }",
                "",
                "    public static class Builder {",
                "        private final List<Middleware> middlewares;",
                "        private CounterReducer counterReducer;",
                "        private Counter counter;",
                "        private TodoListReducer todoListReducer;",
                "        private TodoList todoList;",

                "        public Builder() {",
                "            middlewares = new ArrayList<>();",
                "        }",
                "",
                "        public Builder addMiddleware(Middleware middleware) {",
                "            middlewares.add(middleware);",
                "            return this;",
                "        }",
                "",
                "        public Builder setReducer(CounterReducer counterReducer) {",
                "            this.counterReducer = counterReducer;",
                "            return this;",
                "        }",
                "",
                "        public Builder setInitialState(Counter counter) {",
                "            this.counter = counter;",
                "            return this;",
                "        }",
                "",
                "        public Builder setReducer(TodoListReducer todoListReducer) {",
                "            this.todoListReducer = todoListReducer;",
                "            return this;",
                "        }",
                "",
                "        public Builder setInitialState(TodoList todoList) {",
                "            this.todoList = todoList;",
                "            return this;",
                "        }",
                "",
                "        @Override",
                "        public DroiduxRootStore build() {",
                "            if (counterReducer == null) {",
                "                throw new NotInitializedException(\"CounterReducer has not been initialized.\");",
                "            }",
                "            if (counter == null) {",
                "                throw new NotInitializedException(\"Counter has not been initialized.\");",
                "            }",
                "            if (todoListReducer == null) {",
                "                throw new NotInitializedException(\"TodoListReducer has not been initialized.\");",
                "            }",
                "            if (todoList == null) {",
                "                throw new NotInitializedException(\"TodoList has not been initialized.\");",
                "            }",
                "            return new DroiduxRootStore(this);",
                "        }",
                "    }",
                "}"
        };
    }

    public static class DispatchableTakesWrongStateType {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.sample;",
                "import info.izumin.android.droidux.annotation.Dispatchable;",
                "import info.izumin.android.droidux.annotation.Reducer;",
                "import info.izumin.android.droidux.processor.fixture.action.IncrementCountAction;",
                "import info.izumin.android.droidux.processor.fixture.Counter;",
                "@Reducer(Counter.class)",
                "public class CounterReducer {",
                "    @Dispatchable(IncrementCountAction.class)",
                "    public Counter onIncrement(Object state, IncrementCountAction action) {",
                "        return state;",
                "    }",
                "}"
        };
    }

    public static class DispatchableTakesWrongActionType {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.sample;",
                "import info.izumin.android.droidux.annotation.Dispatchable;",
                "import info.izumin.android.droidux.annotation.Reducer;",
                "import info.izumin.android.droidux.processor.fixture.action.AddTodoItemAction;",
                "import info.izumin.android.droidux.processor.fixture.CompleteTodoItemAction;",
                "import info.izumin.android.droidux.processor.fixture.TodoList;",
                "@Reducer(TodoList.class)",
                "public class TodoListReducer {",
                "    @Dispatchable(AddTodoItemAction.class)",
                "    public TodoList onAddItem(TodoList state, CompleteTodoItemAction action) {",
                "        return state;",
                "    }",
                "}"
        };
    }

    public static class DispatchableTakesExtraArguments {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.sample;",
                "import info.izumin.android.droidux.annotation.Dispatchable;",
                "import info.izumin.android.droidux.annotation.Reducer;",
                "import info.izumin.android.droidux.processor.fixture.action.IncrementCountAction;",
                "import info.izumin.android.droidux.processor.fixture.Counter;",
                "@Reducer(Counter.class)",
                "public class CounterReducer {",
                "    @Dispatchable(IncrementCountAction.class)",
                "    public Counter onIncrement(Counter state, IncrementCountAction action, String extra) {",
                "        return state;",
                "    }",
                "}"
        };
    }

    public static class ReducerWithoutSuffix {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.sample;",
                "import info.izumin.android.droidux.annotation.Dispatchable;",
                "import info.izumin.android.droidux.annotation.Reducer;",
                "import info.izumin.android.droidux.processor.fixture.action.IncrementCountAction;",
                "import info.izumin.android.droidux.processor.fixture.Counter;",
                "@Reducer(Counter.class)",
                "public class CounterReduce {",
                "    @Dispatchable(IncrementCountAction.class)",
                "    public Counter onIncrement(Counter state, IncrementCountAction action, String extra) {",
                "        return state;",
                "    }",
                "}"
        };
    }

    public static class UndoableReducerWithoutUndoableState {
        public static final String[] TARGET = {
                "package info.izumin.android.droidux.sample;",
                "import info.izumin.android.droidux.annotation.Dispatchable;",
                "import info.izumin.android.droidux.annotation.Reducer;",
                "import info.izumin.android.droidux.annotation.Undoable;",
                "import info.izumin.android.droidux.processor.fixture.action.IncrementCountAction;",
                "import info.izumin.android.droidux.processor.fixture.Counter;",
                "@Undoable",
                "@Reducer(Counter.class)",
                "public class CounterReducer {",
                "}"
        };
    }
}
