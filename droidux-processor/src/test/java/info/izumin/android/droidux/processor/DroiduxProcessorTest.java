package info.izumin.android.droidux.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.tools.JavaFileObject;

import info.izumin.android.droidux.processor.fixture.Source;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by izumin on 11/2/15.
 */
public class DroiduxProcessorTest {
    public static final String TAG = DroiduxProcessorTest.class.getSimpleName();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static void assertJavaSource(JavaFileObject target, JavaFileObject generated) {
        assert_().about(javaSource())
                .that(target)
                .processedWith(new DroiduxProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(generated);
    }

    @Test
    public void oneActionDispatchableReducer() {
        assertJavaSource(
                forSourceLines("CounterReducer", Source.Counter.TARGET),
                forSourceLines("DroiduxCounterStore", Source.Counter.GENERATED)
        );
    }

    @Test
    public void dispatchableMethdoTakesOnlyStateArgument() {
        assertJavaSource(
                forSourceLines("CounterReducer", Source.CounterTakesOnlyStateArgument.TARGET),
                forSourceLines("DroiduxCounterStore", Source.CounterTakesOnlyStateArgument.GENERATED)
        );
    }

    @Test
    public void twoActionDispatchableReducer() {
        assertJavaSource(
                forSourceLines("TodoListReducer", Source.TodoList.TARGET),
                forSourceLines("DroiduxTodoListStore", Source.TodoList.GENERATED)
        );
    }

    @Test
    public void combinedTwoReducers() {
        assertJavaSource(
                forSourceLines("RootReducer", Source.CombinedTwoReducers.TARGET),
                forSourceLines("DroiduxRootStore", Source.CombinedTwoReducers.GENERATED)
        );
    }

    @Test
    public void undoableReducer() {
        assertJavaSource(
                forSourceLines("UndoableTodoListReducer", Source.UndoableTodoList.TARGET),
                forSourceLines("DroiduxUndoableTodoListStore", Source.UndoableTodoList.GENERATED)
        );
    }

    @Test
    public void dispatchableMethodTakesWrongStateType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("1st argument of CounterReducer#onIncrement() does not match the @Reducer value.");
        assertJavaSource(
                forSourceLines("CounterReducer", Source.DispatchableTakesWrongStateType.TARGET),
                forSourceLines("DroiduxCounterReducer", Source.EMPTY)
        );
    }

    @Test
    public void dispatchableMethodTakesWrongActionType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("2nd argument of TodoListReducer#onAddItem() does not match the @Dispatchable value.");
        assertJavaSource(
                forSourceLines("TodoListReducer", Source.DispatchableTakesWrongActionType.TARGET),
                forSourceLines("DroiduxTodoListReducer", Source.EMPTY)
        );
    }

    @Test
    public void dispatchableMethodTakesNoArguments() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("CounterReducer#onIncrement() must take 2 arguments.");
        assertJavaSource(
                forSourceLines("CounterReducer", Source.DispatchableTakesNoArguments.TARGET),
                forSourceLines("DroiduxCounterReducer", Source.EMPTY)
        );
    }

    @Test
    public void dispatchableMethodTakesExtraArguments() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("CounterReducer#onIncrement() must take 2 arguments.");
        assertJavaSource(
                forSourceLines("CounterReducer", Source.DispatchableTakesExtraArguments.TARGET),
                forSourceLines("DroiduxTodoListReducer", Source.EMPTY)
        );
    }

    @Test
    public void reducerWithoutSuffix() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Class name of annotated class with @Reducer must be end with \"Reducer\".");
        assertJavaSource(
                forSourceLines("CounterReduce", Source.ReducerWithoutSuffix.TARGET),
                forSourceLines("DroiduxTodoListReduce", Source.EMPTY)
        );
    }

    @Test
    public void undoableReducerWithoutUndoableState() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("State class for undoable reducer must implement \"UndoableState<T>\".");
        assertJavaSource(
                forSourceLines("CounterReduce", Source.UndoableReducerWithoutUndoableState.TARGET),
                forSourceLines("DroiduxTodoListReduce", Source.EMPTY)
        );
    }
}
