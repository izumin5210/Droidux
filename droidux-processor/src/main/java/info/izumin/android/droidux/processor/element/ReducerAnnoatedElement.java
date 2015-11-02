package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.findMethodsByAnnotation;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassNameFromAnnotation;

/**
 * Created by izumin on 11/2/15.
 */
public class ReducerAnnoatedElement {
    public static final String TAG = ReducerAnnoatedElement.class.getSimpleName();

    private static final String CLASS_NAME_PREFIX = "Droidux";
    private static final String STORE_CLASS_NAME_SUFFIX = "Store";

    private final TypeElement typeElement;
    private final StoreClassElement storeElement;

    private ClassName reducerType;

    public ReducerAnnoatedElement(TypeElement typeElement) {
        this.typeElement = typeElement;
        reducerType = getClassNameFromAnnotation(typeElement, Reducer.class, "value");
        storeElement = new StoreClassElement(reducerType,
                typeElement.getQualifiedName().toString(),
                findMethodsByAnnotation(typeElement, Dispatchable.class));
    }

    public JavaFile createJavaFile() {
        return storeElement.createJavaFile();
    }
}
