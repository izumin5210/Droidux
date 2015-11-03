package info.izumin.android.droidux;

import rx.Observable;

/**
 * Created by izumin on 11/2/15.
 */
public class Action {
    public static final String TAG = Action.class.getSimpleName();

    public Observable<Action> call(Store store) {
        return Observable.just(this);
    }
}
