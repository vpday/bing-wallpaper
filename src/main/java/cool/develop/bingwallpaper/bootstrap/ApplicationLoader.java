package cool.develop.bingwallpaper.bootstrap;

import com.blade.Blade;
import com.blade.mvc.Const;

/**
 * @author vpday
 * @date 2019/1/7
 */
public final class ApplicationLoader {

    private static Blade blade;

    private ApplicationLoader() {
    }

    public static void init(Blade blade) {
        ApplicationLoader.blade = blade;
        loadConfig();
    }

    /**
     * 重新设置配置文件加载路径，以便打包后随时更改配置
     */
    private static void loadConfig() {
        String path = "file:" + Const.CLASSPATH + "application.properties";
        blade.environment().add("boot_conf", path);
    }
}
