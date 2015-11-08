package info.izumin.android.droidux.processor.util;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import static com.google.auto.common.AnnotationMirrors.getAnnotationElementAndValue;
import static com.google.auto.common.MoreElements.getAnnotationMirror;
import static info.izumin.android.droidux.processor.util.StringUtils.getClassName;
import static info.izumin.android.droidux.processor.util.StringUtils.getPackageName;

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

    public static ClassName getClassFromAnnotation(Element element, Class<? extends Annotation> annotationClass, String argName) {
        return getClassesFromAnnotation(element, annotationClass, argName).get(0);
    }

    public static List<ClassName> getClassesFromAnnotation(Element element, Class<? extends Annotation> annotationType, String argName) {
        AnnotationMirror mirror = getAnnotationMirror(element, annotationType).orNull();
        if (mirror == null) { return new ArrayList<>(); }
        Map.Entry<ExecutableElement, AnnotationValue> entry = getAnnotationElementAndValue(mirror, argName);
        List<String> names = new ArrayList<>();
        if (entry.getValue().getValue() instanceof Iterable) {
            for (Object value : (List) entry.getValue().getValue()) {
                names.add(value.toString().replaceAll("\\.class$", ""));
            }
        } else {
                names.add(entry.getValue().getValue().toString().replaceAll("\\.class$", ""));
        }
        List<ClassName> classes = new ArrayList<>();
        for (String name : names) {
            classes.add(ClassName.get(getPackageName(name), getClassName(name)));
        }
        return classes;
    }
}