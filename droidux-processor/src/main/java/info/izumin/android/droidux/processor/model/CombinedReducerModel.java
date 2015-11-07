package info.izumin.android.droidux.processor.model;

import com.google.auto.common.MoreTypes;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.CombinedReducer;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassesFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class CombinedReducerModel extends ReducerModel {
    public static final String TAG = CombinedReducerModel.class.getSimpleName();

    private final CombinedStoreModel combinedStoreModel;

    public CombinedReducerModel(TypeElement element, List<TypeElement> reducerElements) {
        super(element);
        List<ReducerModel> reducerModels = new ArrayList<>();
        for (Class reducer : getClassesFromAnnotation(element, CombinedReducer.class, "value")) {
            for (TypeElement reducerElement : reducerElements) {
                if (MoreTypes.isTypeOf(reducer, reducerElement.asType())) {
                    reducerModels.add(new ReducerModel(reducerElement));
                }
            }
        }
        this.combinedStoreModel = new CombinedStoreModel(this, reducerModels);
    }

    public CombinedStoreModel getCombinedStoreModel() {
        return combinedStoreModel;
    }
}
