package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.UndoableState;
import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.annotation.Undoable;
import info.izumin.android.droidux.processor.exception.InvalidClassNameException;
import info.izumin.android.droidux.processor.exception.InvalidStateClassException;
import info.izumin.android.droidux.processor.util.StringUtils;

import static com.google.auto.common.MoreTypes.asTypeElement;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.findMethodsByAnnotation;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getTypeFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class ReducerModel {
    public static final String TAG = ReducerModel.class.getSimpleName();

    private static final String CLASS_NAME_SUFFIX = "Reducer";

    private final TypeElement element;
    private ClassName state;
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
        if (element.getAnnotation(Reducer.class) != null) {
            this.state = ClassName.get(asTypeElement(getTypeFromAnnotation(element, Reducer.class, "value")));
            this.stateName = state.simpleName();
            this.stateVariableName = StringUtils.getLowerCamelFromUpperCamel(stateName);
        }

        isUndoable = element.getAnnotation(Undoable.class) != null;

        if (isUndoable) {
            try {
                if (!UndoableState.class.isAssignableFrom(Class.forName(state.packageName() + "." + state.simpleName()))) {
                    throw new InvalidStateClassException("State class for undoable reducer must implement \"UndoableState<T>\".");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        this.className = ClassName.get(element);

        this.qualifiedName = element.getQualifiedName().toString();
        this.packageName = StringUtils.getPackageName(qualifiedName);
        this.variableName = StringUtils.getLowerCamelFromUpperCamel(className.simpleName());

        if (!className.simpleName().endsWith(CLASS_NAME_SUFFIX)) {
            throw new InvalidClassNameException("Class name of annotated class with @Reducer must be end with \"Reducer\".");
        }

        this.dispatchableModels = new ArrayList<>();
        for (ExecutableElement el : findMethodsByAnnotation(element, Dispatchable.class)) {
            dispatchableModels.add(new DispatchableModel(el, this));
        }
    }

    public TypeElement getElement() {
        return element;
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
