package info.izumin.android.droidux.action

import info.izumin.android.droidux.UndoableState
import info.izumin.android.droidux.annotation.Reducer
import info.izumin.android.droidux.annotation.Undoable
import spock.lang.Specification

/**
 * Created by izumin on 12/5/15.
 */
class HistoryActionTest extends Specification {

    class Counter implements UndoableState<Counter> {
        @Override Counter clone() {
            return new Counter()
        }
    }

    class SubUndoableDummyReducer extends UndoableDummyReducer {}

    @Undoable @Reducer(Counter.class)
    class UndoableDummyReducer {}

    @Reducer(String.class)
    class DummyReducer {}

    class DummyClass {}

    def "when pass the class annotated with @Reducer and @Undoable to the constructor"() {
        when:
        new HistoryAction(HistoryAction.Kind.UNDO, UndoableDummyReducer.class)

        then:
        noExceptionThrown()
    }

    def "when pass the class is not annotated with @Reducer to the constructor"() {
        when:
        new HistoryAction(HistoryAction.Kind.UNDO, DummyReducer.class)

        then:
        thrown(IllegalArgumentException.class)
    }

    def "when pass the class that has no annotations to the constructor"() {
        when:
        new HistoryAction(HistoryAction.Kind.UNDO, DummyClass.class)

        then:
        thrown(IllegalArgumentException.class)
    }

    def "isAssignableTo()"() {
        setup:
        def action = new HistoryAction(HistoryAction.Kind.UNDO, UndoableDummyReducer.class)

        expect:
        action.isAssignableTo(reducer) == result

        where:
        reducer                         | result
        new UndoableDummyReducer()      | true
        new Object()                    | false
        new DummyReducer()              | false
        new SubUndoableDummyReducer()   | false
    }
}
