package info.izumin.android.droidux.processor.util;

import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

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

    public static List<TypeElement> findClassesByAnnotation(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        List<TypeElement> list = new ArrayList<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(clazz);
        for (Element el : elements) {
            if (el instanceof TypeElement) {
                list.add((TypeElement) el);
            }
        }
        return list;
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

    public static ClassName getClassNameFromAnnotation(Element element, Class<? extends Annotation> annotationClass, String argName) {
        AnnotationMirror am = getAnnotationMirror(element, annotationClass);
        AnnotationValue av = getAnnotationValue(am, argName);
        if (av != null) {
            String name = av.getValue().toString();
            return ClassName.get(getPackageName(name), getClassName(name));
        } else {
            return null;
        }
    }

    public static AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationClass) {
        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            if (am.getAnnotationType().toString().equals(annotationClass.getCanonicalName())) {
                return am;
            }
        }
        return null;
    }

    public static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String argName) {
        if (annotationMirror == null) { return null; }
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(argName)) {
                return entry.getValue();
            }
        }
        return null;
    }
}