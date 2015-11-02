package info.izumin.android.droidux.processor.fixture;

/**
 * Created by izumin on 11/2/15.
 */
public class Counter {
    public static final String TAG = Counter.class.getSimpleName();

    private int count;

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
