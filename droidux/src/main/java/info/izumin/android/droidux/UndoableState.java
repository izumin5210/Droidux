package info.izumin.android.droidux;

/**
 * Created by izumin on 11/24/15.
 */
public interface UndoableState<T> extends Cloneable {
    T clone();
}
