package info.izumin.android.droidux.processor.generator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.processor.model.BuilderModel;
import info.izumin.android.droidux.processor.model.DispatcherModel;
import info.izumin.android.droidux.processor.model.StoreImplModel;
import info.izumin.android.droidux.processor.model.StoreMethodModel;
import info.izumin.android.droidux.processor.model.StoreModel;

import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;

/**
 * Created by izumin on 11/3/15.
 */
public class StoreClassGenerator {
    public static final String TAG = StoreClassGenerator.class.getSimpleName();

    private final StoreModel storeModel;

    public StoreClassGenerator(StoreModel storeModel) {
        this.storeModel = storeModel;
    }

    public JavaFile createJavaFile() {
        return JavaFile.builder(storeModel.getClassName().packageName(), createTypeSpec())
                .skipJavaLangImports(true).build();
    }

    private TypeSpec createTypeSpec() {
        return TypeSpec.classBuilder(storeModel.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(storeModel.getInterfaceName())
                .addFields(createFieldSpecs())
                .addMethod(createConstructor())
                .addMethods(createGetterMethodSpecs())
                .addType(new StoreBuilderClassGenerator(storeModel).createBuilderTypeSpec())
                .build();
    }

    private List<FieldSpec> createFieldSpecs() {
        List<FieldSpec> specs = new ArrayList<>();
        specs.addAll(FluentIterable.from(storeModel.getStoreImplModels())
                .transform(new Function<StoreImplModel, FieldSpec>() {
                    @Override
                    public FieldSpec apply(StoreImplModel input) {
                        return FieldSpec.builder(input.getClassName(), input.getVariableName(),
                                Modifier.PRIVATE, Modifier.FINAL).build();
                    }
                }).toList());
        specs.add(DispatcherModel.fieldSpec());
        return specs;
    }

    private MethodSpec createConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(getParameterSpec(storeModel.getBuilderModel().getClassName()));

        for (StoreImplModel storeImpl : storeModel.getStoreImplModels()) {
            builder = builder.addStatement("$N = new $T($N.$N, $N.$N)",
                    storeImpl.getVariableName(), storeImpl.getClassName(),
                    BuilderModel.VARIABLE_NAME, storeImpl.getStateVariableName(),
                    BuilderModel.VARIABLE_NAME, storeImpl.getReducerModel().getVariableName());
        }

        final String middlewareFiledName = "middleware";
        return builder
                .beginControlFlow("for ($T $N : $N.$N)", Middleware.class, middlewareFiledName,
                        BuilderModel.VARIABLE_NAME, StoreModel.MIDDLEWARES_FIELD_NAME)
                .addStatement("$N.$N(this)",
                        middlewareFiledName, StoreModel.ATTACH_MIDDLEWARE_METHOD_NAME)
                .endControlFlow()
                .addStatement("$N = new $N($N.$N, $N)",
                        DispatcherModel.VARIABLE_NAME, DispatcherModel.CLASS_NAME,
                        BuilderModel.VARIABLE_NAME, BuilderModel.MIDDLEWARES_VARIABLE_NAME,
                        FluentIterable.from(storeModel.getStoreImplModels())
                                .transform(new Function<StoreImplModel, String>() {
                                    @Override
                                    public String apply(StoreImplModel input) {
                                        return input.getVariableName();
                                    }
                                }).join(Joiner.on(", ")))
                .build();
    }

    private List<MethodSpec> createGetterMethodSpecs() {
        return FluentIterable.from(storeModel.getMethodModels())
                .transform(new Function<StoreMethodModel, MethodSpec>() {
                    @Override
                    public MethodSpec apply(StoreMethodModel input) {
                        return MethodSpec.methodBuilder(input.getName())
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.get(input.getReturnType()))
                                .addParameters(input.getParameters())
                                .addCode(input.getCodeBlock())
                                .build();
                    }
                })
                .toList();
    }
}
