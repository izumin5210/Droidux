package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.ExecutableElement;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.processor.exception.InvalidMethodArgumentsException;

import static com.google.auto.common.MoreTypes.asTypeElement;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getTypeFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class DispatchableModel {
    public static final String TAG = DispatchableModel.class.getSimpleName();

    private final ExecutableElement element;
    private final ClassName action;
    private final String methodName;

    public DispatchableModel(ExecutableElement element, ReducerModel reducerModel) {
        this.element = element;
        this.methodName = element.getSimpleName().toString();
        this.action = ClassName.get(asTypeElement(getTypeFromAnnotation(element, Dispatchable.class, "value")));

        String displayName = reducerModel.getClassName() + "#" + element.getSimpleName() + "()";

        if (element.getParameters().size() < 1 || element.getParameters().size() > 2) {
            throw new InvalidMethodArgumentsException(displayName + " must take 2 arguments.");
        }

        if (!ClassName.get(element.getParameters().get(0).asType()).equals(reducerModel.getState())) {
            throw new InvalidMethodArgumentsException("1st argument of " + displayName + " does not match the @Reducer value.");
        }

        if (element.getParameters().size() == 2 && !ClassName.get(element.getParameters().get(1).asType()).equals(action)) {
            throw new InvalidMethodArgumentsException("2nd argument of " + displayName + " does not match the @Dispatchable value.");
        }
    }

    public ClassName getAction() {
        return action;
    }

    public String getMethodName() {
        return methodName;
    }

    public int argumentCount() {
        return element.getParameters().size();
    }
}
