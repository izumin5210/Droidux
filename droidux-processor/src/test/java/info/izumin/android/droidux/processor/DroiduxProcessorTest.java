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

    private static void assertJavaSource(JavaFileObject target, JavaFileObject generated, JavaFileObject... rest) {
        assert_().about(javaSource())
                .that(target)
                .processedWith(new DroiduxProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(generated, rest);
    }

    @Test
    public void singleReducer() {
        assertJavaSource(
                forSourceLines("RootStore", Source.Counter.TARGET),
                forSourceLines("DroiduxRootStore_CounterStoreImpl", Source.StoreImpl.COUNTER),
                forSourceLines("DroiduxRootStore", Source.Counter.GENERATED_STORE)
        );
    }

    @Test
    public void singleReducerBindable() {
        assertJavaSource(
                forSourceLines("RootStore", Source.BindableCounter.TARGET),
                forSourceLines("DroiduxRootStore_CounterStoreImpl", Source.StoreImpl.COUNTER),
                forSourceLines("DroiduxRootStore", Source.BindableCounter.GENERATED_STORE)
        );
    }

    @Test
    public void combinedTwoReducers() {
        assertJavaSource(
                forSourceLines("RootStore", Source.CombinedTwoReducers.TARGET),
                forSourceLines("DroiduxRootStore_CounterStoreImpl", Source.StoreImpl.COUNTER),
                forSourceLines("DroiduxRootStore_TodoListStoreImpl", Source.StoreImpl.TODO_LIST),
                forSourceLines("DroiduxRootStore", Source.CombinedTwoReducers.GENERATED)
        );
    }

    @Test
    public void combinedReducerAndBindableReducer() {
        assertJavaSource(
                forSourceLines("RootStore", Source.CombinedReducerAndBindableReducer.TARGET),
                forSourceLines("DroiduxRootStore_CounterStoreImpl", Source.StoreImpl.COUNTER),
                forSourceLines("DroiduxRootStore_TodoListStoreImpl", Source.StoreImpl.TODO_LIST),
                forSourceLines("DroiduxRootStore", Source.CombinedReducerAndBindableReducer.GENERATED)
        );
    }

    @Test
    public void dispatchableMethodTakesWrongStateType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "@Dispatchable method can have arguments only state or action. "
                        + "CounterReducer#increment() has more than one invalid argument."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.DispatchableTakesWrongStateType.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }

    @Test
    public void dispatchableMethodTakesWrongActionType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "@Dispatchable method can have arguments only state or action. "
                        + "TodoListReducer#addItem() has more than one invalid argument."
        );
        assertJavaSource(
                forSourceLines("TodoListStore", Source.DispatchableTakesWrongActionType.TARGET),
                forSourceLines("DroiduxTodoListStore_TodoListStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxTodoListStore", Source.EMPTY)
        );
    }


    @Test
    public void dispatchableMethodReturnsWrongType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "@Dispatchable method can have arguments only state or action. "
                        + "CounterReducer#increment() has more than one invalid argument."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.DispatchableTakesExtraArguments.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }

    @Test
    public void dispatchableMethodShouldReturnState() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "@Dispatchable method must return new state. "
                        + "But CounterReducer#increment() returns invalid type."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.DispatchableMethosReturnsWrongType.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }

    @Test
    public void reducerWithoutSuffix() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "@Reducer class name must end with \"Reducer\". \"CounterReduce\" has invalid class name."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.ReducerWithoutSuffix.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }

    @Test
    public void undoableReducerWithoutUndoableState() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "@Reducer class annotated with @Undoable must have the state implements \"UndoableState<T>\". "
                        + "Counter state of CounterReducer does not implement it."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.UndoableReducerWithoutUndoableState.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }

    @Test
    public void storeHasClassThatIsNotAnnotatedWithReducer() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "Values of @Store annotation must have only classes annotated with \"@Reducer\"."
                        + "But CounterStore has invalid value."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.StoreHasInvalidValue.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }

    @Test
    public void storeNotExtendBaseStore() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(
                "The interface that is annotated @Store must extend \"BaseStore\"."
        );
        assertJavaSource(
                forSourceLines("CounterStore", Source.StoreNotExtendsBaseStore.TARGET),
                forSourceLines("DroiduxCounterStore_CounterStoreImpl", Source.EMPTY),
                forSourceLines("DroiduxCounterStore", Source.EMPTY)
        );
    }
}
