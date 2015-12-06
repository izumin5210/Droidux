package info.izumin.android.droidux.processor.validator;

import com.google.auto.common.MoreTypes;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import info.izumin.android.droidux.UndoableState;
import info.izumin.android.droidux.processor.exception.InvalidReducerDeclarationException;
import info.izumin.android.droidux.processor.model.ReducerModel;

/**
 * Created by izumin on 11/29/15.
 */
public final class ReducerValidator {
    private ReducerValidator() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static void validate(ReducerModel model) {
        if (!isValidClassName(model)) {
            throw new InvalidReducerDeclarationException(
                    "@Reducer class name must end with \"" + ReducerModel.CLASS_NAME_SUFFIX + "\". "
                            + "\"" + model.getClassName().simpleName() + "\" has invalid class name."
            );
        }

        if (model.isUndoable() && !hasUndoableState(model.getStateElement())) {
            throw new InvalidReducerDeclarationException(
                    "@Reducer class annotated with @Undoable must have the state implements \"UndoableState<T>\". "
                            + model.getState().simpleName() + " state of " + model.getClassName().simpleName() + " does not implement it."
            );
        }
    }

    private static boolean isValidClassName(ReducerModel model) {
        return model.getClassName().simpleName().endsWith(ReducerModel.CLASS_NAME_SUFFIX);
    }

    private static boolean hasUndoableState(TypeElement stateElement) {
        return FluentIterable.from(stateElement.getInterfaces())
                .transform(new Function<TypeMirror, TypeName>() {
                    @Override
                    public TypeName apply(TypeMirror input) {
                        return ClassName.get(MoreTypes.asTypeElement(input));
                    }
                })
                .contains(TypeName.get(UndoableState.class));
    }
}