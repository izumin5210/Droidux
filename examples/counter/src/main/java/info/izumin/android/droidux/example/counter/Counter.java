package info.izumin.android.droidux.example.counter;

/**
 * Created by izumin on 12/6/15.
 */
public class Counter {
    public static final String TAG = Counter.class.getSimpleName();

    private final int count;

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
