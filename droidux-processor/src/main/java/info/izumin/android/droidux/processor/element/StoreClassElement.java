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
                .superclass(ParameterizedTypeName.get(ClassName.get(Store.class), storeModel.getState()))
                .addField(reducerModel.getReducer(), reducerModel.getVariableName(), Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(createConstructor())
                .addMethod(createMethodSpec())
                .addType(new StoreBuilderClassElement(storeModel).createBuilderTypeSpec())
                .build();
    }

    private MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(getParameterSpec(storeModel.getBuilder()))
                .addStatement("super($N)", storeModel.getBuilderVariableName())
                .addStatement("this.$N = $N.$N",
                        reducerModel.getVariableName(), storeModel.getBuilderVariableName(),
                        reducerModel.getVariableName())
                .addStatement("setState($N.$N)",
                        storeModel.getBuilderVariableName(), storeModel.getStateVariableName())
                .build();
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
            builder = builder
                    .beginControlFlow("if (actionClass.isAssignableFrom($T.class))", dispatchableModel.getAction())
//                    .addStatement("result = $N.$N(getState())", reducerModel.getVariableName(), dispatchableModel.getMethodName())
                    .addStatement("result = $N.$N(getState(), ($T) action)",
                            reducerModel.getVariableName(), dispatchableModel.getMethodName(), dispatchableModel.getAction())
                    .endControlFlow();
        }

        return builder
                .beginControlFlow("if (result != null)")
                .addStatement("setState(result)")
                .endControlFlow()
                .build();
    }
}
