package info.izumin.android.droidux

import rx.Observable
import spock.lang.Specification;

/**
 * Created by izumin on 11/24/15.
 */
class StoreTest extends Specification {
    static class StoreImpl extends Store {
        protected StoreImpl(Builder builder) {
            super(builder)
        }

        @Override
        protected void dispatchToReducer(Action action) {

        }

        static class Builder extends Store.Builder {
            @Override
            Builder addMiddleware(Middleware middleware) {
                super.addMiddleware(middleware)
                return this
            }

            @Override
            Store build() {
                new StoreImpl(this)
            }
        }
    }

    def middleware1
    def middleware2
    def store
    def action

    def setup() {
        middleware1 = Mock(Middleware.class)
        middleware2 = Mock(Middleware.class)
        action = Mock(Action.class)

        store = new StoreImpl.Builder()
                .addMiddleware(middleware1)
                .addMiddleware(middleware2)
                .build()
    }

    def "#dispatch()"() {
        when:
        store.dispatch(action).subscribe()

        then:
        1 * middleware1.beforeDispatch(action) >> Observable.just(action)
        1 * middleware2.beforeDispatch(action) >> Observable.just(action)
        1 * middleware2.afterDispatch(action) >> Observable.just(action)
        1 * middleware1.afterDispatch(action) >> Observable.just(action)
    }
}
