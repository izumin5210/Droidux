package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.History;
import info.izumin.android.droidux.Store;
import info.izumin.android.droidux.action.HistoryAction;
import info.izumin.android.droidux.processor.model.DispatchableModel;
import info.izumin.android.droidux.processor.model.ReducerModel;
import info.izumin.android.droidux.processor.model.StoreModel;

import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreClassElement {
    public static final String TAG = StoreClassElement.class.getSimpleName();

    private static final String DISPATCH_TO_REDUCER_METHOD_NAME = "dispatchToReducer";
    private static final String HISTORY_VARIABLE_NAME = "history";
    private static final String HISTORY_SIZE_SETTER_METHOD_NAME = "setHistorySize";

    private final ReducerModel reducerModel;
    private final StoreModel storeModel;

    public StoreClassElement(ReducerModel reducerModel) {
        this.reducerModel = reducerModel;
        this.storeModel = reducerModel.getStoreModel();
    }

    public JavaFile createJavaFile() {
        return JavaFile.builder(storeModel.getPackageName(), createTypeSpec())
                .skipJavaLangImports(true).build();
    }

    private TypeSpec createTypeSpec() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(storeModel.getClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(Store.class), storeModel.getState()))
                .addField(reducerModel.getReducer(), reducerModel.getVariableName(), Modifier.PRIVATE, Modifier.FINAL);

        if (storeModel.isUndoable()) {
            ParameterizedTypeName historyFieldName = ParameterizedTypeName.get(ClassName.get(History.class), storeModel.getState());
            builder = builder.addField(historyFieldName, HISTORY_VARIABLE_NAME, Modifier.PRIVATE, Modifier.FINAL);
        }

        builder = builder.addMethod(createConstructor())
                .addMethod(createMethodSpec());

        if (storeModel.isUndoable()) {
            builder = builder.addMethod(createHistorySizeSetterMethodSpec());
        }

        return builder.addType(new StoreBuilderClassElement(storeModel).createBuilderTypeSpec())
                .build();
    }

    private MethodSpec createConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(getParameterSpec(storeModel.getBuilder()))
                .addStatement("super($N)", storeModel.getBuilderVariableName())
                .addStatement("this.$N = $N.$N",
                        reducerModel.getVariableName(), storeModel.getBuilderVariableName(),
                        reducerModel.getVariableName());

        if (storeModel.isUndoable()) {
            builder = builder.addStatement("this.$N = new $T<>($N.$N)",
                    HISTORY_VARIABLE_NAME, History.class,
                    storeModel.getBuilderVariableName(), storeModel.getStateVariableName())
                    .addStatement("setState($N.getPresent())", HISTORY_VARIABLE_NAME);
        } else {
            builder = builder.addStatement("setState($N.$N)",
                            storeModel.getBuilderVariableName(), storeModel.getStateVariableName());
        }

        return builder.build();
    }

    private MethodSpec createMethodSpec() {
        return MethodSpec.methodBuilder(DISPATCH_TO_REDUCER_METHOD_NAME)
                .addAnnotation(getOverrideAnnotation())
                .addModifiers(Modifier.PROTECTED)
                .returns(TypeName.VOID)
                .addParameter(getParameterSpec(Action.class))
                .addCode(createCodeBlock())
                .build();
    }

    private CodeBlock createCodeBlock() {
        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("Class<? extends Action> actionClass = action.getClass()")
                .addStatement(storeModel.getStateName() + " result = null");

        for (DispatchableModel dispatchableModel : reducerModel.getDispatchableModels()) {
            builder = builder.beginControlFlow("if (actionClass.isAssignableFrom($T.class))", dispatchableModel.getAction());
            if (dispatchableModel.argumentCount() == 2) {
                builder = builder.addStatement("result = $N.$N(getState(), ($T) action)",
                        reducerModel.getVariableName(), dispatchableModel.getMethodName(), dispatchableModel.getAction());
            } else {
                builder = builder.addStatement("result = $N.$N(getState())",
                        reducerModel.getVariableName(), dispatchableModel.getMethodName());
            }
            if (storeModel.isUndoable()) {
                builder = builder.addStatement("$N.insert(result)", HISTORY_VARIABLE_NAME);
            }
            builder = builder.endControlFlow();
        }

        if (storeModel.isUndoable()) {
            builder = builder.beginControlFlow("if (actionClass.isAssignableFrom($T.class))", HistoryAction.class)
                    .addStatement("$T historyAction = ($T) action", HistoryAction.class, HistoryAction.class)
                    .beginControlFlow("if (historyAction.isAssignableTo(this))")
                    .addStatement("result = historyAction.handle(history)")
                    .endControlFlow()
                    .endControlFlow();
        }

        return builder
                .beginControlFlow("if (result != null)")
                .addStatement("setState(result)")
                .endControlFlow()
                .build();
    }

    private MethodSpec createHistorySizeSetterMethodSpec() {
        return MethodSpec.methodBuilder(HISTORY_SIZE_SETTER_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.INT, "size")
                .addStatement("$N.setLimit(size)", HISTORY_VARIABLE_NAME)
                .build();
    }
}
