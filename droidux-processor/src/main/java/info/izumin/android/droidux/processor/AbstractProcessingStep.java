package info.izumin.android.droidux.processor;

import com.google.auto.common.BasicAnnotationProcessor;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;

import javax.annotation.processing.Filer;

/**
 * Created by izumin on 11/26/15.
 */
public abstract class AbstractProcessingStep implements BasicAnnotationProcessor.ProcessingStep {
    public static final String TAG = AbstractProcessingStep.class.getSimpleName();

    private final Filer filer;

    public AbstractProcessingStep(Filer filer) {
        this.filer = filer;
    }

    protected void write(JavaFile file) {
        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
