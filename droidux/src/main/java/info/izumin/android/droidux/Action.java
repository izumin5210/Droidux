package info.izumin.android.droidux;

import rx.Observable;

/**
 * Created by izumin on 11/2/15.
 */
public class Action<T> {
    public static final String TAG = Action.class.getSimpleName();

    private final T value;

    public Action(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Observable<Action> call(Store store) {
        return Observable.just((Action) this);
    }
}
