package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.List;

import info.izumin.android.droidux.Middleware;

import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;

/**
 * Created by izumin on 11/28/15.
 */
public class BuilderModel {
    public static final String TAG = BuilderModel.class.getSimpleName();

    public static final String CLASS_NAME = "Builder";
    public static final String VARIABLE_NAME = getLowerCamelFromUpperCamel(CLASS_NAME);

    public static final String ADD_MIDDLEWARE_METHOD_NAME = "addMiddleware";
    public static final String STATE_SETTER_METHOD_NAME = "setInitialState";
    public static final String REDUCER_SETTER_METHOD_NAME = "setReducer";
    public static final String BUILD_METHOD_NAME = "build";

    public static final ParameterizedTypeName MIDDLEWARES_TYPE =
            ParameterizedTypeName.get(List.class, Middleware.class);
    public static final String MIDDLEWARES_VARIABLE_NAME = "middlewares";

    private final ClassName className;

    private final StoreModel storeModel;
    private final List<ReducerModel> reducerModels;

    public BuilderModel(StoreModel storeModel) {
        this.storeModel = storeModel;
        this.className = storeModel.getClassName().nestedClass(CLASS_NAME);
        this.reducerModels = storeModel.getReducerModels();
    }

    public StoreModel getStoreModel() {
        return storeModel;
    }

    public ClassName getClassName() {
        return className;
    }

    public List<ReducerModel> getReducerModels() {
        return reducerModels;
    }
}
