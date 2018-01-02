package info.izumin.android.droidux.thunk

import info.izumin.android.droidux.Action
import info.izumin.android.droidux.Dispatcher
import info.izumin.android.droidux.Middleware
import info.izumin.android.droidux.StoreImpl
import io.reactivex.Flowable
import spock.lang.Specification

/**
 * Created by izumin on 11/29/15.
 */
class ThunkMiddlewareTest extends Specification {

    def store1
    def store2
    def thunkMiddleware
    def middleware1
    def middleware2
    def preAction
    def action
    def asyncAction
    def dispatcher

    def setup() {
        store1 = Mock(StoreImpl.class, constructorArgs: [null, null])
        store2 = Mock(StoreImpl.class, constructorArgs: [null, null])
        thunkMiddleware = new ThunkMiddleware()
        middleware1 = Mock(Middleware.class)
        middleware2 = Mock(Middleware.class)
        preAction = Mock(Action.class)
        action = Mock(Action.class)
        asyncAction = new AsyncAction() {
            @Override
            Flowable<Action> call(Dispatcher d) {
                d.dispatch(preAction).flatMap({_a -> Flowable.just(action) }) as Flowable<Action>
            }
        }

        def middlewares = new ArrayList<Middleware>()
        middlewares.add(middleware1)
        middlewares.add(thunkMiddleware)
        middlewares.add(middleware2)

        dispatcher = new Dispatcher(middlewares, store1, store2)
        thunkMiddleware.onAttach(null, dispatcher)
    }

    def "dispatch AsyncAction"() {
        when:
        dispatcher.dispatch(asyncAction).subscribe()

        then:
        1 * middleware1.beforeDispatch(asyncAction) >> Flowable.just(asyncAction)

        then:
        1 * middleware1.beforeDispatch(preAction) >> Flowable.just(preAction)

        then:
        1 * middleware2.beforeDispatch(preAction) >> Flowable.just(preAction)

        then:
        1 * store1.dispatch(preAction)

        then:
        1 * store2.dispatch(preAction)

        then:
        1 * middleware2.afterDispatch(preAction) >> Flowable.just(preAction)

        then:
        1 * middleware1.afterDispatch(preAction) >> Flowable.just(preAction)

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

        then:
        1 * middleware2.beforeDispatch(asyncAction) >> Flowable.just(asyncAction)

        then:
        1 * store1.dispatch(asyncAction)

        then:
        1 * store2.dispatch(asyncAction)

        then:
        1 * middleware2.afterDispatch(asyncAction) >> Flowable.just(asyncAction)

        then:
        1 * middleware1.afterDispatch(asyncAction) >> Flowable.just(asyncAction)
    }
}
