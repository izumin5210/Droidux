package info.izumin.android.droidux.processor.validator;

import com.google.auto.common.MoreTypes;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import javax.lang.model.type.TypeMirror;

import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.annotation.Store;
import info.izumin.android.droidux.processor.exception.InvalidStoreDelcarationException;
import info.izumin.android.droidux.processor.model.StoreModel;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.getTypesFromAnnotation;

/**
 * Created by izumin on 11/29/15.
 */
public final class StoreValidator {
    private StoreValidator() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static void validate(StoreModel model) {
        if (!hasAnnotatedReducers(model)) {
            throw new InvalidStoreDelcarationException(
                    "Values of @Store annotation must have only classes annotated with \"@Reducer\"."
                            + "But " + model.getInterfaceName().simpleName() + " has invalid value."
            );
        }
    }

    private static boolean hasAnnotatedReducers(StoreModel model) {
        return FluentIterable.from(getTypesFromAnnotation(model.getElement(), Store.class, "value"))
                .filter(new Predicate<TypeMirror>() {
                    @Override
                    public boolean apply(TypeMirror input) {
                        return MoreTypes.asTypeElement(input).getAnnotation(Reducer.class) == null;
                    }
                }).size() == 0;
    }
}