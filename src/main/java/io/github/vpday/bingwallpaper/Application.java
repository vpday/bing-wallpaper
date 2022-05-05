package io.github.vpday.bingwallpaper;

import com.hellokaton.blade.Blade;
import com.hellokaton.blade.security.xss.XssMiddleware;
import io.github.vpday.bingwallpaper.bootstrap.ApplicationLoader;
import io.github.vpday.bingwallpaper.hooks.CustomCsrfMiddleware;

/**
 * @author vpday
 * @date 2018/11/23
 */
public class Application {

    public static void main(String[] args) {
        Blade blade = Blade.of();
        ApplicationLoader.init(blade);
        blade.use(new XssMiddleware(), new CustomCsrfMiddleware()).start(Application.class, args);
    }

}
