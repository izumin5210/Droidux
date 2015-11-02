package info.izumin.android.droidux.processor.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Action;
import info.izumin.android.droidux.Store;
import info.izumin.android.droidux.Store.Builder;
import info.izumin.android.droidux.annotation.Dispatchable;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassNameFromAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getOverrideAnnotation;
import static info.izumin.android.droidux.processor.util.PoetUtils.getParameterSpec;
import static info.izumin.android.droidux.processor.util.StringUtils.getClassName;
import static info.izumin.android.droidux.processor.util.StringUtils.getPackageName;
import static info.izumin.android.droidux.processor.util.StringUtils.replaceSuffix;

/**
 * Created by izumin on 11/2/15.
 */
public class StoreClassElement {
    public static final String TAG = StoreClassElement.class.getSimpleName();

    private static final String CLASS_NAME_PREFIX = "Droidux";
    private static final String CLASS_NAME_SUFFIX = "Store";
    private static final String REDUCER_CLASS_NAME_SUFFIX = "Reducer";
    private static final String DISPATCH_TO_REDUCERS_METHOD_NAME = "dispatchToReducers";

    private ClassName reducerType;
    private final String packageName;
    private final String className;
    private final String storeClassName;
    private final List<ExecutableElement> dispatchableMethods;

    public StoreClassElement(ClassName reducerType, String reducerQualifiedName, List<ExecutableElement> dispatchableMethods) {
        this.reducerType = reducerType;
        this.dispatchableMethods = dispatchableMethods;
        this.packageName = getPackageName(reducerQualifiedName);
        this.className = getClassName(reducerQualifiedName);
        this.storeClassName = CLASS_NAME_PREFIX + replaceSuffix(className, REDUCER_CLASS_NAME_SUFFIX, CLASS_NAME_SUFFIX);
    }

    public JavaFile createJavaFile() {
        return JavaFile.builder(packageName, createTypeSpec()).skipJavaLangImports(true).build();
    }

    private TypeSpec createTypeSpec() {
        return TypeSpec.classBuilder(storeClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(Store.class), reducerType.box()))
                .addMethod(createConstructor())
                .addMethod(createMethodSpec())
                .addType(new StoreBuilderClassElement(storeClassName, packageName).createBuilderTypeSpec())
                .build();
    }

    private MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(getParameterSpec(Builder.class))
                .addCode("super(builder);\n")
                .build();
    }

    private MethodSpec createMethodSpec() {
        return MethodSpec.methodBuilder(DISPATCH_TO_REDUCERS_METHOD_NAME)
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
                .addStatement(reducerType.simpleName() + " result = null")
                .beginControlFlow("for (Object reducer : getReducers())")
                .beginControlFlow("if (reducer instanceof " + className + ")")
                .addStatement(className + " r = (" + className + ") reducer");

        for (ExecutableElement el : dispatchableMethods) {
            // FIXME: 決め打ちになってて良くない
            ClassName name = getClassNameFromAnnotation(el, Dispatchable.class, "value");
            builder = builder
                    .beginControlFlow("if (actionClass.isAssignableFrom($T.class))", name)
                    .addStatement("result = r." + el.getSimpleName() + "(getState())")
                    .endControlFlow();
        }

        return builder
                .endControlFlow()
                .endControlFlow()
                .beginControlFlow("if (result != null)")
                .addStatement("setState(result)")
                .endControlFlow()
                .build();
    }
}
