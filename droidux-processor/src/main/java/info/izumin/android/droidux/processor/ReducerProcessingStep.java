package info.izumin.android.droidux.processor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

import info.izumin.android.droidux.annotation.Reducer;

/**
 * Created by izumin on 11/26/15.
 */
public class ReducerProcessingStep extends AbstractProcessingStep {
    public static final String TAG = ReducerProcessingStep.class.getSimpleName();

    public ReducerProcessingStep(Filer filer) {
        super(filer);
    }

    @Override
    public Set<? extends Class<? extends Annotation>> annotations() {
        return ImmutableSet.<Class<? extends Annotation>>of(Reducer.class);
    }

    @Override
    public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
//        for (Element element : elementsByAnnotation.get(Reducer.class)) {
//            write(new StoreImplClassGenerator(new ReducerModel((TypeElement) element)).createJavaFile());
//        }
        return new HashSet<>();
    }
}
