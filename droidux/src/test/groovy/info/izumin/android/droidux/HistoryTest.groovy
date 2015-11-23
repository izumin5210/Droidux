package info.izumin.android.droidux

import spock.lang.Specification;

/**
 * Created by izumin on new Counter(1)new Counter(1)/new Counter(2)new Counter(4)/new Counter(1)new Counter(5).
 */
class HistoryTest extends Specification {

    def history
    def past
    def future

    static class Counter implements UndoableState<Counter> {
        def count

        Counter(count) {
            this.count = count
        }

        @Override
        boolean equals(Object o) {
            return (o instanceof Counter) && (o.hashCode() == hashCode())
        }

        @Override
        int hashCode() {
            return count
        }

        @Override
        Counter clone() {
            return new Counter(count)
        }
    }

    def setup() {
        history = new History<Counter>(new Counter(0))
        past = History.metaClass.getAttribute(history, "past")
        future = History.metaClass.getAttribute(history, "future")
    }

    def getPresent() {
        History.metaClass.getAttribute(history, "present")
    }

    def "#insert()"() {
        when:
        history.insert(new Counter(1))

        then:
        getPresent() == new Counter(1)

        when:
        history.insert(new Counter(3))

        then:
        getPresent() == new Counter(3)
    }

    def "#undo()"() {
        when:
        history.insert(new Counter(1))

        then:
        past.size() == 1
        history.undo() == new Counter(0)
        past.size() == 0

        when:
        history.insert(new Counter(3))
        history.insert(new Counter(5))

        then:
        past.size() == 2
        history.undo() == new Counter(3)
        past.size() == 1
        history.undo() == new Counter(0)
        past.size() == 0
        history.undo() == new Counter(0)
    }

    def "#redo()"() {
        when:
        history.insert(new Counter(1))
        history.undo()

        then:
        future.size() == 1
        history.redo() == new Counter(1)
        future.size() == 0

        when:
        history.insert(new Counter(3))
        history.insert(new Counter(5))
        history.undo()
        history.undo()

        then:
        future.size() == 2
        history.redo() == new Counter(3)
        future.size() == 1
        history.redo() == new Counter(5)
        future.size() == 0
        history.redo() == new Counter(5)

        when:
        history.insert(new Counter(8))
        history.insert(new Counter(7))
        history.undo()
        history.undo()

        then:
        future.size() == 2
        history.insert(new Counter(4))
        future.size() == 0
    }

    def "#setLimit()"() {
        when:
        history.setLimit(3)
        history.insert(new Counter(1))

        then:
        past.size() == 1

        when:
        history.insert(new Counter(2))

        then:
        past.size() == 2

        when:
        history.insert(new Counter(3))

        then:
        past.size() == 3

        when:
        history.insert(new Counter(4))

        then:
        past.size() == 3
        history.undo() == new Counter(3)
        history.undo() == new Counter(2)
        history.undo() == new Counter(1)
        history.undo() == new Counter(1)
    }
}
