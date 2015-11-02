package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Store;
import info.izumin.android.droidux.processor.model.ReducerModel;
import info.izumin.android.droidux.processor.model.StoreModel;

import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreBuilderClassElement {
    public static final String TAG = StoreBuilderClassElement.class.getSimpleName();

    private static final String ADD_REDUCER_METHOD_NAME = "addReducer";
    private static final String BUILD_METHOD_NAME = "build";

    private final StoreModel storeModel;
    private final ReducerModel reducerModel;

    public StoreBuilderClassElement(StoreModel storeModel) {
        this.storeModel = storeModel;
        this.reducerModel = storeModel.getReducerModel();
    }

    public TypeSpec createBuilderTypeSpec() {
        return TypeSpec.classBuilder(storeModel.getBuilderName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .superclass(ClassName.get(Store.class).nestedClass(storeModel.getBuilderName()))
                .addField(FieldSpec.builder(reducerModel.getReducer(), reducerModel.getVariableName(), Modifier.PRIVATE).build())
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
                .returns(storeModel.getBuilder())
                .addParameter(getParameterSpec(reducerModel.getReducer()))
                .addStatement("this.$N = $N", reducerModel.getVariableName(), reducerModel.getVariableName())
                .addStatement("return this")
                .build();
    }

    private MethodSpec createBuildMethodSpec() {
        return MethodSpec.methodBuilder(BUILD_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getOverrideAnnotation())
                .returns(storeModel.getStore())
                .addStatement("return new $N(this)", storeModel.getClassName())
                .build();
    }
}
