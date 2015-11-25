package info.izumin.android.droidux.processor.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import info.izumin.android.droidux.annotation.CombinedReducer;

import static com.google.auto.common.MoreTypes.asTypeElement;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassesFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class CombinedReducerModel extends ReducerModel {
    public static final String TAG = CombinedReducerModel.class.getSimpleName();

    private final CombinedStoreModel combinedStoreModel;

    public CombinedReducerModel(TypeElement element) {
        super(element);
        List<ReducerModel> reducerModels = new ArrayList<>();
        for (TypeMirror type : getClassesFromAnnotation(element, CombinedReducer.class, "value")) {
            TypeElement reducerElement = asTypeElement(type);
            reducerModels.add(new ReducerModel(reducerElement));
        }
        this.combinedStoreModel = new CombinedStoreModel(this, reducerModels);
    }

    public CombinedStoreModel getCombinedStoreModel() {
        return combinedStoreModel;
    }
}
