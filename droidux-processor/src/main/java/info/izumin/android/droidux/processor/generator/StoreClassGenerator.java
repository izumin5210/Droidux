package info.izumin.android.droidux.processor.generator;

import android.databinding.BaseObservable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Middleware;
import info.izumin.android.droidux.OnStateChangedListener;
import info.izumin.android.droidux.processor.model.BuilderModel;
import info.izumin.android.droidux.processor.model.DispatcherModel;
import info.izumin.android.droidux.processor.model.StoreImplModel;
import info.izumin.android.droidux.processor.model.StoreMethodModel;
import info.izumin.android.droidux.processor.model.StoreModel;
import rx.Observable;

import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;
import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;

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
                .superclass(TypeName.get(BaseObservable.class))
                .addFields(createFieldSpecs())
                .addMethod(createConstructor())
                .addMethod(createBuilderMethodSpec())
                .addMethods(createGetterMethodSpecs())
                .addMethod(createDispatchMethodSpec())
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

            if (storeImpl.isBindable()) {
                TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(OnStateChangedListener.class), storeImpl.getState()))
                        .addMethod(
                                MethodSpec.methodBuilder(StoreImplModel.ON_STATE_CHANGED_METHOD_NAME)
                                        .addAnnotation(getOverrideAnnotation())
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(getParameterSpec(storeImpl.getState()))
                                        .addStatement("$N($N.$N)",
                                                StoreImplModel.NOTIFY_PROPERTY_CHANGED_METHOD_NAME,
                                                BuilderModel.VARIABLE_NAME,
                                                storeImpl.getFieldIdName())
                                        .build()
                        )
                        .build();
                builder = builder.addStatement("$N.$N($L)", storeImpl.getVariableName(),
                        StoreImplModel.ADD_LISTENER_METHOD_NAME, listener);
            }
        }

        final String middlewareFiledName = "middleware";
        return builder
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
                .beginControlFlow("for ($T $N : $N.$N)", Middleware.class, middlewareFiledName,
                        BuilderModel.VARIABLE_NAME, StoreModel.MIDDLEWARES_FIELD_NAME)
                .addStatement("$N.$N(this, $N)", middlewareFiledName,
                        StoreModel.ATTACH_MIDDLEWARE_METHOD_NAME, DispatcherModel.VARIABLE_NAME)
                .endControlFlow()
                .build();
    }

    private MethodSpec createBuilderMethodSpec() {
        return MethodSpec.methodBuilder(StoreModel.BUILDER_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(storeModel.getBuilderModel().getClassName())
                .addStatement("return new $T()", storeModel.getBuilderModel().getClassName())
                .build();
    }

    private List<MethodSpec> createGetterMethodSpecs() {
        return FluentIterable.from(storeModel.getMethodModels())
                .transform(new Function<StoreMethodModel, MethodSpec>() {
                    @Override
                    public MethodSpec apply(StoreMethodModel input) {
                        return MethodSpec.methodBuilder(input.getName())
                                .addAnnotation(getOverrideAnnotation())
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.get(input.getReturnType()))
                                .addParameters(input.getParameters())
                                .addCode(input.getCodeBlock())
                                .build();
                    }
                })
                .toList();
    }

    private MethodSpec createDispatchMethodSpec() {
        return MethodSpec.methodBuilder(StoreModel.DISPATCH_METHOD_NAME)
                .addAnnotation(getOverrideAnnotation())
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Observable.class), ClassName.get(Action.class)))
                .addParameter(getParameterSpec(Action.class))
                .addStatement("return $N.$N($N)",
                        DispatcherModel.VARIABLE_NAME, DispatcherModel.DISPATCH_METHOD_NAME,
                        getLowerCamelFromUpperCamel(ClassName.get(Action.class).simpleName()))
                .build();
    }
}
