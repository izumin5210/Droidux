package info.izumin.android.droidux.processor.generator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.StoreImpl;
import info.izumin.android.droidux.UndoableStoreImpl;
import info.izumin.android.droidux.processor.model.DispatchableModel;
import info.izumin.android.droidux.processor.model.StoreImplModel;

import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreImplClassGenerator {
    public static final String TAG = StoreImplClassGenerator.class.getSimpleName();

    private final StoreImplModel storeImplModel;

    public StoreImplClassGenerator(StoreImplModel storeImplModel) {
        this.storeImplModel = storeImplModel;
    }

    public JavaFile createJavaFile() {
        return JavaFile.builder(storeImplModel.getPackageName(), createTypeSpec())
                .skipJavaLangImports(true).build();
    }

    private TypeSpec createTypeSpec() {
        return TypeSpec.classBuilder(storeImplModel.getClassName().simpleName())
                .addModifiers(Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(storeImplModel.isUndoable() ? UndoableStoreImpl.class : StoreImpl.class),
                        storeImplModel.getState(), storeImplModel.getReducerModel().getClassName()))
                .addField(storeImplModel.getReducerModel().getClassName(),
                        StoreImplModel.REDUCER_VARIABLE_NAME, Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(createConstructor())
                .addMethod(createMethodSpec())
                .build();
    }

    private MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(storeImplModel.getState(), StoreImplModel.STATE_VARIABLE_NAME)
                .addParameter(storeImplModel.getReducerModel().getClassName(), StoreImplModel.REDUCER_VARIABLE_NAME)
                .addStatement("super($N, $N)", StoreImplModel.STATE_VARIABLE_NAME, StoreImplModel.REDUCER_VARIABLE_NAME)
                .addStatement("this.$N = $N",
                        StoreImplModel.REDUCER_VARIABLE_NAME,
                        StoreImplModel.REDUCER_VARIABLE_NAME)
                .build();
    }

    private MethodSpec createMethodSpec() {
        return MethodSpec.methodBuilder(StoreImplModel.DISPATCH_METHOD_NAME)
                .addAnnotation(getOverrideAnnotation())
                .addModifiers(Modifier.PROTECTED)
                .returns(TypeName.VOID)
                .addParameter(getParameterSpec(Action.class))
                .addCode(createCodeBlock())
                .build();
    }

    private CodeBlock createCodeBlock() {
        CodeBlock.Builder builder = CodeBlock.builder();
        final String ACTION_FIELD =  "action";
        final String ACTION_CLASS_FIELD =  "actionClass";
        final String RESULT_FIELD = "result";
        final String STATE_GETTER = storeImplModel.isUndoable() ? "getState().clone()" : "getState()";

        if (storeImplModel.isUndoable()) {
            builder = builder.addStatement("super.$N($N)",
                    StoreImplModel.DISPATCH_METHOD_NAME, ACTION_FIELD);
        }

        builder = builder.addStatement("Class<? extends $T> $N = $N.getClass()",
                Action.class, ACTION_CLASS_FIELD, ACTION_FIELD)
                .addStatement("$T $N = null", storeImplModel.getState(), RESULT_FIELD);

        for (final DispatchableModel dispatchableModel : storeImplModel.getReducerModel().getDispatchableModels()) {
            final List<Object> args = new ArrayList<>();
            args.add(RESULT_FIELD);
            args.add(StoreImplModel.REDUCER_VARIABLE_NAME);
            args.add(dispatchableModel.getMethodName());
            final String format = "$N = $N.$N(" + FluentIterable.from(dispatchableModel.getArguments())
                    .transform(new Function<ClassName, String>() {
                        @Override
                        public String apply(ClassName input) {
                            if (input.simpleName().equals(dispatchableModel.getAction().simpleName())) {
                                args.add(dispatchableModel.getAction());
                                args.add(ACTION_FIELD);
                                return "($T) $N";
                            } else if (input.simpleName().equals(storeImplModel.getState().simpleName())) {
                                return STATE_GETTER;
                            } else {
                                return "";
                            }
                        }
                    }).join(Joiner.on(", ")) + ")";

            builder = builder.beginControlFlow("if ($T.class.isAssignableFrom($N))",
                    dispatchableModel.getAction(), ACTION_CLASS_FIELD)
                    .addStatement(format, args.toArray())
                    .endControlFlow();
        }

        return builder
                .beginControlFlow("if ($N != null)", RESULT_FIELD)
                .addStatement("setState($N)", RESULT_FIELD)
                .endControlFlow()
                .build();
    }
}
