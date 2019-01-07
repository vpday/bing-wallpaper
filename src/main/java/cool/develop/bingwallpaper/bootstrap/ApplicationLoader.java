package cool.develop.bingwallpaper.bootstrap;

import com.blade.Blade;
import com.blade.mvc.Const;

/**
 * @author vpday
 * @create 2019/1/7
 */
public final class ApplicationLoader {

    private ApplicationLoader() {
    }

    private static Blade blade;

    public static void init(Blade blade) {
        ApplicationLoader.blade = blade;
        loadConfig();
    }

    private static void loadConfig() {
        String path = "file:" + Const.CLASSPATH + "application.properties";
        blade.environment().add("boot_conf", path);
    }
}
