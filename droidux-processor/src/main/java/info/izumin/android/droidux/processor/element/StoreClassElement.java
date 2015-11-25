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
import info.izumin.android.droidux.Store;
import info.izumin.android.droidux.UndoableStore;
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
        return TypeSpec.classBuilder(storeModel.getClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(storeModel.isUndoable() ? UndoableStore.class : Store.class), storeModel.getState()))
                .addField(reducerModel.getReducer(), reducerModel.getVariableName(), Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(createConstructor())
                .addMethod(createMethodSpec())
                .addType(new StoreBuilderClassElement(storeModel).createBuilderTypeSpec())
                .build();
    }

    private MethodSpec createConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(getParameterSpec(storeModel.getBuilder()));

        if (storeModel.isUndoable()) {
            builder = builder.addStatement("super($N, $N.$N)",
                    storeModel.getBuilderVariableName(),
                    storeModel.getBuilderVariableName(), storeModel.getStateVariableName());
        } else {
            builder = builder.addStatement("super($N)", storeModel.getBuilderVariableName());
        }

        builder.addStatement("this.$N = $N.$N",
                        reducerModel.getVariableName(), storeModel.getBuilderVariableName(),
                        reducerModel.getVariableName());

        if (!storeModel.isUndoable()) {
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
        CodeBlock.Builder builder = CodeBlock.builder();

        if (storeModel.isUndoable()) {
            builder = builder.addStatement("super.dispatchToReducer(action)");
        }

        builder = builder.addStatement("Class<? extends Action> actionClass = action.getClass()")
                .addStatement(storeModel.getStateName() + " result = null");

        for (DispatchableModel dispatchableModel : reducerModel.getDispatchableModels()) {
            builder = builder.beginControlFlow("if ($T.class.isAssignableFrom(actionClass))", dispatchableModel.getAction());

            String stateGetter = storeModel.isUndoable() ? "getState().clone()" : "getState()";

            if (dispatchableModel.argumentCount() == 2) {
                builder = builder.addStatement("result = $N.$N(" + stateGetter + ", ($T) action)",
                        reducerModel.getVariableName(), dispatchableModel.getMethodName(), dispatchableModel.getAction());
            } else {
                builder = builder.addStatement("result = $N.$N(" + stateGetter + ")",
                        reducerModel.getVariableName(), dispatchableModel.getMethodName());
            }
            builder = builder.endControlFlow();
        }

        return builder
                .beginControlFlow("if (result != null)")
                .addStatement("setState(result)")
                .endControlFlow()
                .build();
    }
}
