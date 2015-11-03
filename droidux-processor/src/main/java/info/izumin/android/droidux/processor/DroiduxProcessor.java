package info.izumin.android.droidux.processor;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.CombinedReducer;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.processor.element.CombinedStoreClassElement;
import info.izumin.android.droidux.processor.element.StoreClassElement;
import info.izumin.android.droidux.processor.model.CombinedReducerModel;
import info.izumin.android.droidux.processor.model.ReducerModel;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.findClassesByAnnotation;

@AutoService(Processor.class)
public class DroiduxProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{ add(Reducer.class.getName()); add(CombinedReducer.class.getName()); }};
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement element : findClassesByAnnotation(roundEnv, Reducer.class)) {
            try {
                new StoreClassElement(new ReducerModel(element)).createJavaFile().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (TypeElement element : findClassesByAnnotation(roundEnv, CombinedReducer.class)) {
            try {
                new CombinedStoreClassElement(new CombinedReducerModel(element))
                        .createJavaFile().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
