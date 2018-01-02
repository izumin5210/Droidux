package info.izumin.android.droidux.processor.model;

import android.databinding.Bindable;

import com.google.auto.common.MoreTypes;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

import io.reactivex.Observable;

/**
 * Created by izumin on 11/28/15.
 */
public class StoreMethodModel {
    public static final String TAG = StoreMethodModel.class.getSimpleName();

    enum Kind {
        DISPATCH,
        GETTER,
        OBSERVE,
        UNKNOWN
    }

    private static final Class OBSERVE_METHOD_CLASS = Observable.class;

    private final ExecutableElement element;
    private final StoreModel storeModel;
    private final Kind kind;
    private final DeclaredType returnType;
    private boolean isBindable = false;

    public StoreMethodModel(ExecutableElement element, StoreModel storeModel) {
        this.element = element;
        this.storeModel = storeModel;

        List<TypeName> states = FluentIterable.from(storeModel.getStoreImplModels())
                .transform(new Function<StoreImplModel, TypeName>() {
                    @Override
                    public TypeName apply(StoreImplModel input) {
                        return input.getState();
                    }
                }).toList();

        returnType = MoreTypes.asDeclared(element.getReturnType());

        if (states.contains(ClassName.get(returnType))) {
            kind = Kind.GETTER;
            isBindable = element.getAnnotation(Bindable.class) != null;
        } else if (getName().equals(DispatcherModel.DISPATCH_METHOD_NAME)) {
            kind = Kind.DISPATCH;
        } else if (MoreTypes.isTypeOf(OBSERVE_METHOD_CLASS, returnType.asElement().asType())
                && states.contains(ClassName.get(returnType.getTypeArguments().get(0)))) {
            kind = Kind.OBSERVE;
        } else {
            kind = Kind.UNKNOWN;
        }
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public DeclaredType getReturnType() {
        return returnType;
    }

    public CodeBlock getCodeBlock() {
        switch (kind) {
            case GETTER:
                return CodeBlock.builder().addStatement("return $N.$N()",
                        FluentIterable.from(storeModel.getStoreImplModels())
                                .filter(new Predicate<StoreImplModel>() {
                                    @Override
                                    public boolean apply(StoreImplModel input) {
                                        return ClassName.get(returnType).equals(input.getState());
                                    }
                                })
                                .get(0).getVariableName(),
                        StoreImplModel.STATE_GETTER_METHOD_NAME).build();
            case OBSERVE:
                return CodeBlock.builder().addStatement("return $N.$N()",
                        FluentIterable.from(storeModel.getStoreImplModels())
                                .filter(new Predicate<StoreImplModel>() {
                                    @Override
                                    public boolean apply(StoreImplModel input) {
                                        return ClassName.get(returnType.getTypeArguments().get(0)).equals(input.getState());
                                    }
                                })
                                .get(0).getVariableName(),
                        StoreImplModel.STATE_OBSERVE_METHOD_NAME).build();
            case DISPATCH:
                return CodeBlock.builder()
                        .addStatement("return $N.$N($N)", DispatcherModel.VARIABLE_NAME,
                                DispatcherModel.DISPATCH_METHOD_NAME, getParameters().get(0).name)
                        .build();
        }
        return CodeBlock.builder().build();
    }

    public List<ParameterSpec> getParameters() {
        return FluentIterable.from(element.getParameters())
                .transform(new Function<VariableElement, ParameterSpec>() {
                    @Override
                    public ParameterSpec apply(VariableElement input) {
                        return ParameterSpec.builder(TypeName.get(input.asType()),
                                input.getSimpleName().toString()).build();
                    }
                }).toList();
    }

    public boolean isBindable() {
        return isBindable;
    }
}
