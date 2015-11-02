package info.izumin.android.droidux.processor;

import org.junit.Test;

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
    public void twoActionDispatchableReducer() {
        assertJavaSource(
                forSourceLines("TodoListReducer", Source.TodoList.TARGET),
                forSourceLines("DroiduxTodoListStore", Source.TodoList.GENERATED)
        );
    }
}
