package info.izumin.android.droidux.processor.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import static com.google.auto.common.AnnotationMirrors.getAnnotatedAnnotations;
import static com.google.auto.common.AnnotationMirrors.getAnnotationElementAndValue;

/**
 * Created by izumin on 11/2/15.
 */
public final class AnnotationUtils {
    public static final String TAG = AnnotationUtils.class.getSimpleName();

    private AnnotationUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static List<ExecutableElement> findMethodsByAnnotation(Element element, Class<? extends Annotation> clazz) {
        List<ExecutableElement> list = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            Annotation annotation = el.getAnnotation(clazz);
            if (annotation != null) {
                list.add((ExecutableElement) el);
            }
        }
        return list;
    }

    public static Class getClassFromAnnotation(Element element, Class<? extends Annotation> annotationClass, String argName) {
        return getClassesFromAnnotation(element, annotationClass, argName).get(0);
    }

    public static List<Class> getClassesFromAnnotation(Element element, Class<? extends Annotation> annotationType, String argName) {
        List<Class> classes = new ArrayList<>();
        for (AnnotationMirror mirror : getAnnotatedAnnotations(element, annotationType)) {
            Map.Entry<ExecutableElement, AnnotationValue> entry = getAnnotationElementAndValue(mirror, argName);
            if (entry.getValue().getValue() instanceof Iterable) {
                for (Object value : (List) entry.getValue().getValue()) {
                    try {
                        classes.add(Class.forName(value.toString().replaceAll("\\.class$", "")));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (entry.getValue().getValue() instanceof Class) {
                classes.add((Class) entry.getValue().getValue());
            }
        }
        return classes;
    }
}