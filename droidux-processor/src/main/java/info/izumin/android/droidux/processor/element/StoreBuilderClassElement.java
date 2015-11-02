package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Store;

import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreBuilderClassElement {
    public static final String TAG = StoreBuilderClassElement.class.getSimpleName();

    private static final String CLASS_NAME = "Builder";
    private static final String BUILD_METHOD_NAME = "build";

    private final String storeClassName;
    private final String storePackageName;

    public StoreBuilderClassElement(String storeClassName, String storePackageName) {
        this.storeClassName = storeClassName;
        this.storePackageName = storePackageName;
    }

    public TypeSpec createBuilderTypeSpec() {
        return TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .superclass(Store.Builder.class)
                .addMethod(createBuilderConstructor())
                .addMethod(createBuildMethodSpec())
                .build();
    }

    private MethodSpec createBuilderConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(getParameterSpec(Object.class))
                .addCode("super(object);\n")
                .build();
    }

    private MethodSpec createBuildMethodSpec() {
        return MethodSpec.methodBuilder(BUILD_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getOverrideAnnotation())
                .returns(ClassName.get(storePackageName, storeClassName))
                .addCode("return new " + storeClassName + "(this);\n")
                .build();
    }
}
