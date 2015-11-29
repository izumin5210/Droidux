package info.izumin.android.droidux.processor.generator;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.exception.NotInitializedException;
import info.izumin.android.droidux.processor.model.BuilderModel;
import info.izumin.android.droidux.processor.model.ReducerModel;
import info.izumin.android.droidux.processor.model.StoreModel;

import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;
import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreBuilderClassGenerator {
    public static final String TAG = StoreBuilderClassGenerator.class.getSimpleName();

    static final String ERROR_MESSAGE_NOT_INITIALIZED_EXCEPTION = "$T has not been initialized.";

    private final BuilderModel builderModel;

    public StoreBuilderClassGenerator(StoreModel storeModel) {
        builderModel = storeModel.getBuilderModel();
    }

    public TypeSpec createBuilderTypeSpec() {
        return TypeSpec.classBuilder(builderModel.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addFields(createFieldSpecs())
                .addMethod(createAddMiddlewareMethodSpec())
                .addMethod(createBuilderConstructor())
                .addMethods(createReducerSetterMethodSpecs())
                .addMethods(createStateSetterMethodSpecs())
                .addMethod(createBuildMethodSpec())
                .build();
    }

    private List<FieldSpec> createFieldSpecs() {
        List<FieldSpec> specs = new ArrayList<>();
        specs.add(FieldSpec.builder(BuilderModel.MIDDLEWARES_TYPE,
                BuilderModel.MIDDLEWARES_VARIABLE_NAME, Modifier.FINAL, Modifier.PRIVATE).build());
        specs.addAll(FluentIterable.from(builderModel.getReducerModels())
                .transform(new Function<ReducerModel, FieldSpec>() {
                    @Override
                    public FieldSpec apply(ReducerModel input) {
                        return FieldSpec.builder(input.getClassName(),
                                input.getVariableName(), Modifier.PRIVATE).build();
                    }
                })
                .toList());
        specs.addAll(FluentIterable.from(builderModel.getReducerModels())
                .transform(new Function<ReducerModel, FieldSpec>() {
                    @Override
                    public FieldSpec apply(ReducerModel input) {
                        return FieldSpec.builder(input.getState(),
                                input.getStateVariableName(), Modifier.PRIVATE).build();
                    }
                })
                .toList());
        return specs;
    }

    private MethodSpec createBuilderConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N = new $T<>()", BuilderModel.MIDDLEWARES_VARIABLE_NAME, ArrayList.class)
                .build();
    }

    private MethodSpec createAddMiddlewareMethodSpec() {
        return MethodSpec.methodBuilder(BuilderModel.ADD_MIDDLEWARE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(builderModel.getClassName())
                .addParameter(getParameterSpec(Middleware.class))
                .addStatement("$N.add($N)", BuilderModel.MIDDLEWARES_VARIABLE_NAME,
                        getLowerCamelFromUpperCamel(Middleware.class.getName()))
                .addStatement("return this")
                .build();
    }

    private List<MethodSpec> createReducerSetterMethodSpecs() {
        return FluentIterable.from(builderModel.getReducerModels())
                .transform(new Function<ReducerModel, MethodSpec>() {
                    @Override
                    public MethodSpec apply(ReducerModel input) {
                        return MethodSpec.methodBuilder(BuilderModel.REDUCER_SETTER_METHOD_NAME)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(builderModel.getClassName())
                                .addParameter(getParameterSpec(input.getClassName()))
                                .addStatement("this.$N = $N", input.getVariableName(), input.getVariableName())
                                .addStatement("return this")
                                .build();
                    }
                })
                .toList();
    }

    private List<MethodSpec> createStateSetterMethodSpecs() {
        return FluentIterable.from(builderModel.getReducerModels())
                .transform(new Function<ReducerModel, MethodSpec>() {
                    @Override
                    public MethodSpec apply(ReducerModel input) {
                        return MethodSpec.methodBuilder(BuilderModel.STATE_SETTER_METHOD_NAME)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(builderModel.getClassName())
                                .addParameter(getParameterSpec(input.getState()))
                                .addStatement("this.$N = $N", input.getStateVariableName(), input.getStateVariableName())
                                .addStatement("return this")
                                .build();
                    }
                })
                .toList();
    }

    private MethodSpec createBuildMethodSpec() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(BuilderModel.BUILD_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(builderModel.getStoreModel().getClassName());

        for (ReducerModel reducerModel : builderModel.getReducerModels()) {
            builder = builder
                    .beginControlFlow("if ($N == null)", reducerModel.getVariableName())
                    .addStatement("throw new $T(\"" + ERROR_MESSAGE_NOT_INITIALIZED_EXCEPTION + "\")",
                            NotInitializedException.class, reducerModel.getClassName())
                    .endControlFlow();
        }

        return builder
                .addStatement("return new $T(this)", builderModel.getStoreModel().getClassName())
                .build();
    }
}
