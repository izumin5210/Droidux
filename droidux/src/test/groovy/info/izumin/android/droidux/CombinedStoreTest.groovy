package info.izumin.android.droidux

import rx.Observable
import spock.lang.Specification;

/**
 * Created by izumin on 11/24/15.
 */
class CombinedStoreTest extends Specification {
    static class CombinedStoreImpl extends CombinedStore {
        protected CombinedStoreImpl(Builder builder) {
            super(builder)
            for (Store store : builder.stores) {
                addStore(store)
            }
        }

        static class Builder extends Store.Builder {
            def stores

            Builder() {
                stores = new ArrayList<Store>()
            }

            @Override
            Builder addMiddleware(Middleware middleware) {
                super.addMiddleware(middleware)
                return this
            }

            Builder addStore(Store store) {
                stores.add(store)
                return this
            }

            @Override
            CombinedStore build() {
                new CombinedStoreImpl(this)
            }
        }
    }

    static class StoreImpl extends Store {
        protected StoreImpl() {
            super(new Builder())
        }

        @Override
        protected void dispatchToReducer(Action action) {

        }

        static class Builder extends Store.Builder {
            @Override
            Store build() {
                return new StoreImpl()
            }
        }
    }

    def store1
    def store2
    def middleware1
    def middleware2
    def combinedStore
    def action

    def setup() {
        store1 = Mock(StoreImpl.class)
        store2 = Mock(StoreImpl.class)
        middleware1 = Mock(Middleware.class)
        middleware2 = Mock(Middleware.class)
        action = Mock(Action.class)

        combinedStore = new CombinedStoreImpl.Builder()
                .addMiddleware(middleware1)
                .addMiddleware(middleware2)
                .addStore(store1)
                .addStore(store2)
                .build()
    }

    def "#dispatch()"() {
        when:
        combinedStore.dispatch(action).subscribe()

        then:
        1 * middleware1.beforeDispatch(action) >> Observable.just(action)
        1 * middleware2.beforeDispatch(action) >> Observable.just(action)
        1 * store1.dispatch(action) >> Observable.just(action)
        1 * store2.dispatch(action) >> Observable.just(action)
        1 * middleware2.afterDispatch(action) >> Observable.just(action)
        1 * middleware1.afterDispatch(action) >> Observable.just(action)
    }
}
