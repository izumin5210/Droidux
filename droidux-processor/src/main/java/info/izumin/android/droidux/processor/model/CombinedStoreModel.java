package info.izumin.android.droidux.processor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izumin on 11/3/15.
 */
public class CombinedStoreModel extends StoreModel {
    public static final String TAG = CombinedStoreModel.class.getSimpleName();

    private final List<ReducerModel> reducerModels;
    private final List<StoreModel> storeModels;

    public CombinedStoreModel(CombinedReducerModel reducerModel, List<ReducerModel> reducerModels) {
        super(reducerModel);
        this.reducerModels = reducerModels;
        this.storeModels = new ArrayList<>();
        for (ReducerModel subReducerModel : reducerModels) {
            this.storeModels.add(subReducerModel.getStoreModel());
        }
    }

    public List<ReducerModel> getReducerModels() {
        return reducerModels;
    }

    public List<StoreModel> getStoreModels() {
        return storeModels;
    }
}
