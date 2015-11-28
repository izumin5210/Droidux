package info.izumin.android.droidux.processor.model;

import com.google.auto.common.MoreTypes;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import info.izumin.android.droidux.annotation.Dispatchable;

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

    private final List<ClassName> arguments;

    public DispatchableModel(ExecutableElement element, ReducerModel reducerModel) {
        this.element = element;
        this.methodName = element.getSimpleName().toString();
        this.action = ClassName.get(asTypeElement(getTypeFromAnnotation(element, Dispatchable.class, "value")));
        this.arguments = FluentIterable.from(element.getParameters())
                .transform(new Function<VariableElement, ClassName>() {
                    @Override
                    public ClassName apply(VariableElement input) {
                        return ClassName.get(MoreTypes.asTypeElement(input.asType()));
                    }
                }).toList();
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

    public List<ClassName> getArguments() {
        return arguments;
    }
}
