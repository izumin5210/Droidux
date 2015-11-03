package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

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

    static final String ADD_REDUCER_METHOD_NAME = "addReducer";
    static final String ADD_INITIAL_STATE_METHOD_NAME = "addInitialState";
    static final String BUILD_METHOD_NAME = "build";

    private final StoreModel storeModel;
    private final List<ReducerModel> reducerModels;

    public StoreBuilderClassElement(final StoreModel storeModel) {
        this(storeModel, new ArrayList<ReducerModel>() {{
            add(storeModel.getReducerModel());
        }});
    }

    public StoreBuilderClassElement(StoreModel storeModel, List<ReducerModel> reducerModels) {
        this.storeModel = storeModel;
        this.reducerModels = reducerModels;
    }

    public TypeSpec createBuilderTypeSpec() {
        return TypeSpec.classBuilder(storeModel.getBuilderName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .superclass(ClassName.get(Store.class).nestedClass(storeModel.getBuilderName()))
                .addFields(createFieldSpecs())
                .addMethod(createBuilderConstructor())
                .addMethods(createAddReducerMethodSpecs())
                .addMethod(createBuildMethodSpec())
                .build();
    }

    private List<FieldSpec> createFieldSpecs() {
        List<FieldSpec> specs = new ArrayList<>();
        for (ReducerModel reducerModel : reducerModels) {
            specs.add(FieldSpec.builder(
                    reducerModel.getReducer(), reducerModel.getVariableName(), Modifier.PRIVATE
            ).build());
            specs.add(FieldSpec.builder(
                    reducerModel.getState(), reducerModel.getStateVariableName(), Modifier.PRIVATE
            ).build());
        }
        return specs;
    }

    private MethodSpec createBuilderConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super()")
                .build();
    }

    private List<MethodSpec> createAddReducerMethodSpecs() {
        List<MethodSpec> specs = new ArrayList<>();
        for (ReducerModel reducerModel : reducerModels) {
            specs.add(
                    MethodSpec.methodBuilder(ADD_REDUCER_METHOD_NAME)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(storeModel.getBuilder())
                            .addParameter(getParameterSpec(reducerModel.getReducer()))
                            .addStatement("this.$N = $N", reducerModel.getVariableName(), reducerModel.getVariableName())
                            .addStatement("return this")
                            .build()
            );
            specs.add(
                    MethodSpec.methodBuilder(ADD_INITIAL_STATE_METHOD_NAME)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(storeModel.getBuilder())
                            .addParameter(getParameterSpec(reducerModel.getState()))
                            .addStatement("this.$N = $N", reducerModel.getStateVariableName(), reducerModel.getStateVariableName())
                            .addStatement("return this")
                            .build()
            );
        }
        return specs;
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
