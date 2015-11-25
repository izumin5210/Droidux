package info.izumin.android.droidux.processor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.CombinedReducer;
import info.izumin.android.droidux.processor.element.CombinedStoreClassElement;
import info.izumin.android.droidux.processor.model.CombinedReducerModel;

/**
 * Created by izumin on 11/26/15.
 */
public class CombinedReducerProcessingStep extends AbstractProcessingStep {
    public static final String TAG = CombinedReducerProcessingStep.class.getSimpleName();

    public CombinedReducerProcessingStep(Filer filer) {
        super(filer);
    }

    @Override
    public Set<? extends Class<? extends Annotation>> annotations() {
        return ImmutableSet.<Class<? extends Annotation>>of(CombinedReducer.class);
    }

    @Override
    public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
        for (Element element : elementsByAnnotation.get(CombinedReducer.class)) {
            write(new CombinedStoreClassElement(new CombinedReducerModel((TypeElement) element)).createJavaFile());
        }
        return new HashSet<>();
    }
}
