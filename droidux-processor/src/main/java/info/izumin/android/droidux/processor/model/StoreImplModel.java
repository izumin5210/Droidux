package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;
import static info.izumin.android.droidux.processor.util.StringUtils.replaceSuffix;

/**
 * Created by izumin on 11/3/15.
 */
public class StoreImplModel {
    public static final String TAG = StoreImplModel.class.getSimpleName();

    public static final String REDUCER_VARIABLE_NAME = "reducer";
    public static final String STATE_VARIABLE_NAME = "state";
    public static final String DISPATCH_METHOD_NAME = "dispatch";
    public static final String STATE_GETTER_METHOD_NAME = "getState";
    public static final String REDUCER_GETTER_METHOD_NAME = "getReducer";
    public static final String STATE_OBSERVE_METHOD_NAME = "observe";

    private static final String CLASS_NAME_SUFFIX = "StoreImpl";
    private static final String REDUCER_CLASS_NAME_SUFFIX = "Reducer";

    private final ClassName className;

    private final String variableName;
    private final String storeImplName;

    private final StoreModel storeModel;
    private final ReducerModel reducerModel;

    public StoreImplModel(StoreModel storeModel, ReducerModel reducerModel) {
        this.storeModel = storeModel;
        this.reducerModel = reducerModel;

        this.storeImplName = replaceSuffix(reducerModel.getClassName().simpleName(),
                REDUCER_CLASS_NAME_SUFFIX, CLASS_NAME_SUFFIX);

        this.variableName = getLowerCamelFromUpperCamel(storeImplName);
        this.className = ClassName.bestGuess(storeModel.getClassName() + "_" + storeImplName);
    }

    public ClassName getState() {
        return reducerModel.getState();
    }

    public String getPackageName() {
        return storeModel.getClassName().packageName();
    }

    public String getStateName() {
        return reducerModel.getStateName();
    }

    public String getStateVariableName() {
        return reducerModel.getStateVariableName();
    }

    public boolean isUndoable() {
        return reducerModel.isUndoable();
    }

    public String getStoreImplName() {
        return storeImplName;
    }

    public ClassName getClassName() {
        return className;
    }

    public String getVariableName() {
        return variableName;
    }

    public ReducerModel getReducerModel() {
        return reducerModel;
    }
}
