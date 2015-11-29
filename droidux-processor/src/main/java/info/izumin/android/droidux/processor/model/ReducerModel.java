package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.annotation.Undoable;
import info.izumin.android.droidux.processor.util.StringUtils;
import info.izumin.android.droidux.processor.validator.ReducerValidator;

import static com.google.auto.common.MoreTypes.asTypeElement;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.findMethodsByAnnotation;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getTypeFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class ReducerModel {
    public static final String TAG = ReducerModel.class.getSimpleName();

    public static final String CLASS_NAME_SUFFIX = "Reducer";

    private final TypeElement element;
    private final TypeElement stateElement;
    private final ClassName state;
    private final ClassName className;

    private final String qualifiedName;
    private final String packageName;
    private final String variableName;
    private String stateName;
    private String stateVariableName;

    private final boolean isUndoable;

    private List<DispatchableModel> dispatchableModels;

    public ReducerModel(TypeElement element) {
        this.element = element;
        this.stateElement = asTypeElement(getTypeFromAnnotation(element, Reducer.class, "value"));
        this.state = ClassName.get(stateElement);
        this.stateName = state.simpleName();
        this.stateVariableName = StringUtils.getLowerCamelFromUpperCamel(stateName);

        this.isUndoable = element.getAnnotation(Undoable.class) != null;

        this.className = ClassName.get(element);

        this.qualifiedName = element.getQualifiedName().toString();
        this.packageName = StringUtils.getPackageName(qualifiedName);
        this.variableName = StringUtils.getLowerCamelFromUpperCamel(className.simpleName());

        this.dispatchableModels = new ArrayList<>();
        for (ExecutableElement el : findMethodsByAnnotation(element, Dispatchable.class)) {
            dispatchableModels.add(new DispatchableModel(el, this));
        }

        ReducerValidator.validate(this);
    }

    public TypeElement getElement() {
        return element;
    }

    public TypeElement getStateElement() {
        return stateElement;
    }

    public ClassName getState() {
        return state;
    }

    public ClassName getClassName() {
        return className;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateVariableName() {
        return stateVariableName;
    }

    public List<DispatchableModel> getDispatchableModels() {
        return dispatchableModels;
    }

    public boolean isUndoable() {
        return isUndoable;
    }
}
