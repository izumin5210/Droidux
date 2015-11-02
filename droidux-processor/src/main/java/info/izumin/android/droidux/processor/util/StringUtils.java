package info.izumin.android.droidux.processor.util;

/**
 * Created by izumin on 11/2/15.
 */
public final class StringUtils {
    public static final String TAG = StringUtils.class.getSimpleName();

    private StringUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static String getPackageName(String qualifiedName) {
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
    }

    public static String getClassName(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }

    public static String replaceSuffix(String base, String target, String replacement) {
        return base.substring(0, base.lastIndexOf(target)) + replacement;
    }

    public static String getLowerCamelFromUpperCamel(String upperCamel) {
        upperCamel = getClassName(getClassName(upperCamel));
        return upperCamel.substring(0, 1).toLowerCase() + upperCamel.substring(1);
    }
}
