package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

import info.izumin.android.droidux.Dispatcher;

import static info.izumin.android.droidux.processor.util.StringUtils.getLowerCamelFromUpperCamel;

/**
 * Created by izumin on 11/28/15.
 */
public class DispatcherModel {
    public static final String TAG = DispatcherModel.class.getSimpleName();

    public static final Class CLASS = Dispatcher.class;
    public static final String CLASS_NAME = "Dispatcher";
    public static final String VARIABLE_NAME = getLowerCamelFromUpperCamel(CLASS_NAME);

    public static final String DISPATCH_METHOD_NAME = "dispatch";

    public static FieldSpec fieldSpec() {
        return FieldSpec.builder(CLASS, VARIABLE_NAME, Modifier.PRIVATE, Modifier.FINAL).build();
    }
}
