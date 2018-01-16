package info.izumin.android.droidux

import io.reactivex.Flowable
import spock.lang.Specification

/**
 * Created by izumin on 11/24/15.
 */
class DispatcherTest extends Specification {

    def store1
    def store2
    def middleware1
    def middleware2
    def action
    def dispatcher

    def setup() {
        store1 = Mock(StoreImpl.class, constructorArgs: [null, null])
        store2 = Mock(StoreImpl.class, constructorArgs: [null, null])
        middleware1 = Mock(Middleware.class)
        middleware2 = Mock(Middleware.class)
        action = Mock(Action.class)

        def middlewares = new ArrayList<Middleware>()
        middlewares.add(middleware1)
        middlewares.add(middleware2)

        dispatcher = new Dispatcher(middlewares, store1, store2)
    }

    def "#dispatch()"() {
        when:
        dispatcher.dispatch(action).subscribe()

        then:
        1 * middleware1.beforeDispatch(action) >> Flowable.just(action)

        then:
        1 * middleware2.beforeDispatch(action) >> Flowable.just(action)

        then:
        1 * store1.dispatch(action)

        then:
        1 * store2.dispatch(action)

        then:
        1 * middleware2.afterDispatch(action) >> Flowable.just(action)

        then:
        1 * middleware1.afterDispatch(action) >> Flowable.just(action)
    }
}
