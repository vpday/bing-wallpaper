package io.github.vpday.bingwallpaper.bootstrap;

import com.hellokaton.blade.Blade;
import com.hellokaton.blade.mvc.BladeConst;
import com.hellokaton.blade.security.csrf.CsrfMiddleware;
import com.hellokaton.blade.security.csrf.CsrfOptions;

import java.util.LinkedHashSet;

/**
 * @author vpday
 * @date 2019/1/7
 */
public final class ApplicationLoader {

    private ApplicationLoader() {
    }

    public static void init(Blade blade) {
        CsrfOptions csrfOptions = CsrfOptions.create();
        csrfOptions.setExcludeURLs(new LinkedHashSet<>());
        blade.use(new CsrfMiddleware(csrfOptions));

        blade.addStatics("/" + BingWallpaperConst.WALLPAPERS_PATH);
        loadConfig(blade);
    }

    /**
     * 重新设置配置文件加载路径，以便打包后随时更改配置
     */
    private static void loadConfig(Blade blade) {
        String path = "file:" + BladeConst.CLASSPATH + "application.properties";
        blade.environment().add(BladeConst.ENV_KEY_BOOT_CONF, path);
    }
}
