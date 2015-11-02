package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.ExecutableElement;

import info.izumin.android.droidux.annotation.Dispatchable;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassNameFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class DispatchableModel {
    public static final String TAG = DispatchableModel.class.getSimpleName();

    private final ExecutableElement element;
    private final ClassName action;
    private final String methodName;

    public DispatchableModel(ExecutableElement element) {
        this.element = element;
        this.action = getClassNameFromAnnotation(element, Dispatchable.class, "value");
        this.methodName = element.getSimpleName().toString();
    }

    public ClassName getAction() {
        return action;
    }

    public String getMethodName() {
        return methodName;
    }
}
