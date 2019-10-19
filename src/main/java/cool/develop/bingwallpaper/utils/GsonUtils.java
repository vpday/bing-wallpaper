package cool.develop.bingwallpaper.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gson 工具类
 *
 * @author vpday
 * @date 2019/10/19
 */
public final class GsonUtils {

    private static final GsonBuilder INSTANCE = new GsonBuilder();

    private GsonUtils() {
    }

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.serializeNulls();
    }

    public static Gson create() {
        return INSTANCE.create();
    }

}
