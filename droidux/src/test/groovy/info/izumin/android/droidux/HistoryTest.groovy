package info.izumin.android.droidux

import spock.lang.Specification;

/**
 * Created by izumin on 11/24/15.
 */
class HistoryTest extends Specification {

    def history
    def past
    def future

    def setup() {
        history = new History<Integer>(0)
        past = History.metaClass.getAttribute(history, "past")
        future = History.metaClass.getAttribute(history, "future")
    }

    def getPresent() {
        History.metaClass.getAttribute(history, "present")
    }

    def "#insert()"() {
        when:
        history.insert(1)

        then:
        getPresent() == 1

        when:
        history.insert(3)

        then:
        getPresent() == 3
    }

    def "#undo()"() {
        when:
        history.insert(1)

        then:
        past.size() == 1
        history.undo() == 0
        past.size() == 0

        when:
        history.insert(3)
        history.insert(5)

        then:
        past.size() == 2
        history.undo() == 3
        past.size() == 1
        history.undo() == 0
        past.size() == 0
        history.undo() == 0
    }

    def "#redo()"() {
        when:
        history.insert(1)
        history.undo()

        then:
        future.size() == 1
        history.redo() == 1
        future.size() == 0

        when:
        history.insert(3)
        history.insert(5)
        history.undo()
        history.undo()

        then:
        future.size() == 2
        history.redo() == 3
        future.size() == 1
        history.redo() == 5
        future.size() == 0
        history.redo() == 5

        when:
        history.insert(8)
        history.insert(7)
        history.undo()
        history.undo()

        then:
        future.size() == 2
        history.insert(4)
        future.size() == 0
    }

    def "#setLimit()"() {
        when:
        history.setLimit(3)
        history.insert(1)

        then:
        past.size() == 1

        when:
        history.insert(2)

        then:
        past.size() == 2

        when:
        history.insert(3)

        then:
        past.size() == 3

        when:
        history.insert(4)

        then:
        past.size() == 3
        history.undo() == 3
        history.undo() == 2
        history.undo() == 1
        history.undo() == 1
    }
}
