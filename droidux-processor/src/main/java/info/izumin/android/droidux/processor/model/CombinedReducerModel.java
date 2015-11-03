package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import info.izumin.android.droidux.annotation.CombinedReducer;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassNamesFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class CombinedReducerModel extends ReducerModel {
    public static final String TAG = CombinedReducerModel.class.getSimpleName();

    private final CombinedStoreModel combinedStoreModel;

    public CombinedReducerModel(TypeElement element, Elements elements) {
        super(element);
        List<ReducerModel> reducerModels = new ArrayList<>();
        for (ClassName reducer : getClassNamesFromAnnotation(element, CombinedReducer.class, "value")) {
            reducerModels.add(new ReducerModel(elements.getTypeElement(reducer.packageName() + "." + reducer.simpleName())));
        }
        this.combinedStoreModel = new CombinedStoreModel(this, reducerModels);
    }

    public CombinedStoreModel getCombinedStoreModel() {
        return combinedStoreModel;
    }
}
