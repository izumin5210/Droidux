package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;
import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreBuilderClassElement {
    public static final String TAG = StoreBuilderClassElement.class.getSimpleName();

    public static final String CLASS_NAME = "Builder";
    private static final String BUILD_METHOD_NAME = "build";
    private static final String ADD_REDUCER_METHOD_NAME = "addReducer";

    private final String reducerClassName;
    private final String storeClassName;
    private final String storePackageName;
    private final String reducerVariableName;

    private final ClassName reducerType;

    public StoreBuilderClassElement(String reducerClassName, String storeClassName, String storePackageName) {
        this.reducerClassName = reducerClassName;
        this.storeClassName = storeClassName;
        this.storePackageName = storePackageName;
        this.reducerVariableName = getLowerCamelFromUpperCamel(reducerClassName);
        this.reducerType = ClassName.get(storePackageName, reducerClassName);
    }

    public TypeSpec createBuilderTypeSpec() {
        return TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .superclass(ClassName.get(storePackageName, "Store." + CLASS_NAME))
                .addField(FieldSpec.builder(reducerType.box(), reducerVariableName, Modifier.PRIVATE).build())
                .addMethod(createBuilderConstructor())
                .addMethod(createAddReducerMethodSpec())
                .addMethod(createBuildMethodSpec())
                .build();
    }

    private MethodSpec createBuilderConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super()")
                .build();
    }

    private MethodSpec createAddReducerMethodSpec() {
        return MethodSpec.methodBuilder(ADD_REDUCER_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(storePackageName, CLASS_NAME))
                .addParameter(getParameterSpec(reducerType))
                .addStatement("this.$N = $N", reducerVariableName, reducerVariableName)
                .addStatement("return this")
                .build();
    }

    private MethodSpec createBuildMethodSpec() {
        return MethodSpec.methodBuilder(BUILD_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getOverrideAnnotation())
                .returns(ClassName.get(storePackageName, storeClassName))
                .addStatement("return new $N(this)", storeClassName)
                .build();
    }
}
