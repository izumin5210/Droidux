package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;
import static info.izumin.android.droidux.processor.util.StringUtils.replaceSuffix;

/**
 * Created by izumin on 11/3/15.
 */
public class StoreModel {
    public static final String TAG = StoreModel.class.getSimpleName();

    private static final String CLASS_NAME_PREFIX = "Droidux";
    private static final String CLASS_NAME_SUFFIX = "Store";
    private static final String REDUCER_CLASS_NAME_SUFFIX = "Reducer";
    private static final String BUILDER_CLASS_NAME = "Builder";

    private final ClassName state;
    private final ClassName store;
    private final ClassName builder;

    private final String packageName;
    private final String storeName;
    private final String className;
    private final String variableName;
    private final String stateName;
    private final String builderName;
    private final String builderVariableName;

    private final ReducerModel reducerModel;

    public StoreModel(ReducerModel reducerModel) {
        this.reducerModel = reducerModel;
        this.state = reducerModel.getState();
        this.packageName = reducerModel.getPackageName();
        this.storeName =
                replaceSuffix(reducerModel.getClassName(), REDUCER_CLASS_NAME_SUFFIX, CLASS_NAME_SUFFIX);
        this.className = CLASS_NAME_PREFIX + storeName;
        this.variableName = getLowerCamelFromUpperCamel(storeName);
        this.stateName = reducerModel.getStateName();
        this.store = ClassName.get(packageName, className);
        this.builderName = BUILDER_CLASS_NAME;
        this.builder = store.nestedClass(builderName);
        this.builderVariableName = getLowerCamelFromUpperCamel(builderName);
    }

    public ClassName getState() {
        return state;
    }

    public ClassName getStore() {
        return store;
    }

    public ClassName getBuilder() {
        return builder;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getClassName() {
        return className;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getStateName() {
        return stateName;
    }

    public String getBuilderName() {
        return builderName;
    }

    public String getBuilderVariableName() {
        return builderVariableName;
    }

    public ReducerModel getReducerModel() {
        return reducerModel;
    }
}
