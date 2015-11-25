package info.izumin.android.droidux.processor.util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;
import static com.google.auto.common.MoreElements.getAnnotationMirror;

/**
 * Created by izumin on 11/2/15.
 */
public final class AnnotationUtils {
    public static final String TAG = AnnotationUtils.class.getSimpleName();

    private AnnotationUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static List<ExecutableElement> findMethodsByAnnotation(Element element, final Class<? extends Annotation> clazz) {
        return FluentIterable.from(element.getEnclosedElements())
                .filter(new Predicate<Element>() {
                    @Override
                    public boolean apply(Element input) {
                        return input.getAnnotation(clazz) != null;
                    }
                })
                .transform(new Function<Element, ExecutableElement>() {
                    @Override
                    public ExecutableElement apply(Element input) {
                        return (ExecutableElement) input;
                    }
                })
                .toList();
    }

    public static TypeMirror getTypeFromAnnotation(Element element, Class<? extends Annotation> annotationType, String argName) {
        AnnotationMirror am = getAnnotationMirror(element, annotationType).get();
        AnnotationValue av = getAnnotationValue(am, argName);
        return TO_TYPE.visit(av);
    }

    public static List<TypeMirror> getTypesFromAnnotation(Element element, Class<? extends Annotation> annotationType, String argName) {
        AnnotationMirror am = getAnnotationMirror(element, annotationType).get();
        AnnotationValue av = getAnnotationValue(am, argName);
        return TO_LIST_OF_TYPE.visit(av);
    }

    private static final AnnotationValueVisitor<ImmutableList<TypeMirror>, Void> TO_LIST_OF_TYPE = new SimpleAnnotationValueVisitor6<ImmutableList<TypeMirror>, Void>() {
        @Override
        public ImmutableList<TypeMirror> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
            return FluentIterable.from(vals).transform(new Function<AnnotationValue, TypeMirror>() {
                @Override
                public TypeMirror apply(AnnotationValue input) {
                    return TO_TYPE.visit(input);
                }
            }).toList();
        }
    };

    private static final AnnotationValueVisitor<TypeMirror, Void> TO_TYPE = new SimpleAnnotationValueVisitor6<TypeMirror, Void>() {
        @Override
        public TypeMirror visitType(TypeMirror t, Void aVoid) {
            return t;
        }
    };
}