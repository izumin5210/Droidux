package info.izumin.android.droidux.processor;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;

@AutoService(Processor.class)
public class DroiduxProcessor extends BasicAnnotationProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    protected Iterable<? extends ProcessingStep> initSteps() {
        return ImmutableList.<ProcessingStep>of(
                new ReducerProcessingStep(getFiler()),
                new StoreProcessingStep(getFiler())
        );
    }

    private Filer getFiler() {
        return processingEnv.getFiler();
    }
}
