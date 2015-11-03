package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.CombinedStore;
import info.izumin.android.droidux.processor.model.CombinedReducerModel;
import info.izumin.android.droidux.processor.model.CombinedStoreModel;
import info.izumin.android.droidux.processor.model.ReducerModel;
import info.izumin.android.droidux.processor.model.StoreModel;

import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;

/**
 * Created by izumin on 11/3/15.
 */
public class CombinedStoreClassElement {
    public static final String TAG = CombinedStoreClassElement.class.getSimpleName();

    private final CombinedReducerModel reducerModel;
    private final CombinedStoreModel storeModel;

    public CombinedStoreClassElement(CombinedReducerModel reducerModel) {
        this.reducerModel = reducerModel;
        this.storeModel = reducerModel.getCombinedStoreModel();
    }

    public JavaFile createJavaFile() {
        return JavaFile.builder(storeModel.getPackageName(), createTypeSpec())
                .skipJavaLangImports(true).build();
    }

    private TypeSpec createTypeSpec() {
        return TypeSpec.classBuilder(storeModel.getClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get(CombinedStore.class))
                .addFields(createFieldSpecs())
                .addMethod(createConstructor())
                .addMethods(createGetterMethodSpecs())
                .addType(new StoreBuilderClassElement(storeModel, storeModel.getReducerModels()).createBuilderTypeSpec())
                .build();
    }

    private List<FieldSpec> createFieldSpecs() {
        List<FieldSpec> specs = new ArrayList<>();
        for (ReducerModel reducer: storeModel.getReducerModels()) {
            specs.add(FieldSpec.builder(reducer.getReducer(), reducer.getVariableName(),
                            Modifier.PRIVATE, Modifier.FINAL).build());
            specs.add(FieldSpec.builder(
                    reducer.getStoreModel().getStore(),
                    reducer.getStoreModel().getVariableName(),
                    Modifier.PRIVATE, Modifier.FINAL).build());
        }
        return specs;
    }

    private MethodSpec createConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(getParameterSpec(storeModel.getBuilder()))
                .addStatement("super($N)", storeModel.getBuilderVariableName());

        for (ReducerModel reducer : storeModel.getReducerModels()) {
            builder = builder.addStatement("this.$N = builder.$N", reducer.getVariableName(), reducer.getVariableName())
                    .addStatement("this.$N = new $N.Builder().addReducer($N).build()",
                            reducer.getStoreModel().getVariableName(), reducer.getStoreModel().getClassName(), reducer.getVariableName());
        }

        return builder.build();
    }

    private List<MethodSpec> createGetterMethodSpecs() {
        List<MethodSpec> specs = new ArrayList<>();
        for (ReducerModel reducer : storeModel.getReducerModels()) {
            StoreModel store = reducer.getStoreModel();
            specs.add(
                    MethodSpec.methodBuilder("get" + store.getStoreName())
                            .addModifiers(Modifier.PUBLIC)
                            .returns(store.getStore())
                            .addStatement("return $N", store.getVariableName())
                            .build()
            );
        }
        return specs;
    }
}
