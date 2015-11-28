package info.izumin.android.droidux.processor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.Store;
import info.izumin.android.droidux.processor.generator.StoreClassGenerator;
import info.izumin.android.droidux.processor.generator.StoreImplClassGenerator;
import info.izumin.android.droidux.processor.model.StoreImplModel;
import info.izumin.android.droidux.processor.model.StoreModel;

/**
 * Created by izumin on 11/26/15.
 */
public class StoreProcessingStep extends AbstractProcessingStep {
    public static final String TAG = StoreProcessingStep.class.getSimpleName();

    public StoreProcessingStep(Filer filer) {
        super(filer);
    }

    @Override
    public Set<? extends Class<? extends Annotation>> annotations() {
        return ImmutableSet.<Class<? extends Annotation>>of(Store.class);
    }

    @Override
    public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
        for (Element element : elementsByAnnotation.get(Store.class)) {
            StoreModel model = new StoreModel((TypeElement) element);
            for (StoreImplModel storeImplModel : model.getStoreImplModels()) {
                write(new StoreImplClassGenerator(storeImplModel).createJavaFile());
            }
            write(new StoreClassGenerator(model).createJavaFile());
        }
        return new HashSet<>();
    }
}
