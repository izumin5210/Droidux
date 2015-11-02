package info.izumin.android.droidux.processor.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Modifier;

import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;

/**
 * Created by izumin on 11/2/15.
 */
public final class PoetUtils {
    public static final String TAG = PoetUtils.class.getSimpleName();

    private PoetUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static ParameterSpec getParameterSpec(Class<?> clazz, Modifier... modifiers) {
        return ParameterSpec.builder(
                TypeName.get(clazz),
                getLowerCamelFromUpperCamel(clazz.getSimpleName()),
                modifiers
        ).build();
    }

    public static AnnotationSpec getOverrideAnnotation() {
        return getAnnotationSpec(Override.class);
    }

    public static AnnotationSpec getAnnotationSpec(Class<? extends Annotation> clazz) {
        return AnnotationSpec.builder(clazz).build();
    }
}
