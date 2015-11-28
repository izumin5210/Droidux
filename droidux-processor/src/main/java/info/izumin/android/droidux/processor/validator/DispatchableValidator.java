package info.izumin.android.droidux.processor.validator;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.VariableElement;

import info.izumin.android.droidux.processor.exception.InvalidDispatchableDeclarationException;
import info.izumin.android.droidux.processor.model.DispatchableModel;

import static com.google.auto.common.MoreTypes.asTypeElement;

/**
 * Created by izumin on 11/29/15.
 */
public final class DispatchableValidator {
    private DispatchableValidator() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static void validate(DispatchableModel model) {
        for (VariableElement element : model.getElement().getParameters()) {
            if (!isAction(model, element) && !isState(model, element)) {
                throw new InvalidDispatchableDeclarationException(
                        "@Dispatchable method can have arguments only state or action. "
                                + methodName(model) + " has more than one invalid argument."
                );
            }
        }

        if (!isValidReturnType(model)) {
            throw new InvalidDispatchableDeclarationException(
                    "@Dispatchable method must return new state. "
                            + "But " + methodName(model) + " returns invalid type."
            );
        }
    }

    private static boolean isAction(DispatchableModel model, VariableElement element) {
        return model.getAction().equals(ClassName.get(asTypeElement(element.asType())));
    }

    private static boolean isState(DispatchableModel model, VariableElement element) {
        return model.getState().equals(ClassName.get(asTypeElement(element.asType())));
    }

    private static boolean isValidReturnType(DispatchableModel model) {
        return ClassName.get(asTypeElement(model.getElement().getReturnType()))
                .equals(model.getState());
    }

    private static String argName(VariableElement element) {
        return "[" + element.asType().toString() + " " + element.getSimpleName() + "]";
    }

    private static String methodName(DispatchableModel model) {
        return model.getReducerClassName().simpleName() + "#" + model.getMethodName() + "()";
    }
}