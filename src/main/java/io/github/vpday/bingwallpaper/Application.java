package io.github.vpday.bingwallpaper;

import com.hellokaton.blade.Blade;
import com.hellokaton.blade.security.csrf.CsrfMiddleware;
import io.github.vpday.bingwallpaper.bootstrap.ApplicationLoader;
import io.github.vpday.bingwallpaper.hooks.BaseWebHook;

/**
 * @author vpday
 * @date 2018/11/23
 */
public class Application {

    public static void main(String[] args) {
        Blade blade = Blade.create();
        ApplicationLoader.init(blade);
        blade.use(new BaseWebHook(), new CsrfMiddleware()).start(Application.class, args);
    }

}
