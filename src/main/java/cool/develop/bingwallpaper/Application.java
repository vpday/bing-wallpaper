package cool.develop.bingwallpaper;

import com.blade.Blade;
import cool.develop.bingwallpaper.bootstrap.ApplicationLoader;
import cool.develop.bingwallpaper.hooks.CustomCsrfMiddleware;

/**
 * @author vpday
 * @create 2018/11/23
 */
public class Application {

    public static void main(String[] args) {
        Blade blade = Blade.of();
        ApplicationLoader.init(blade);
        blade.use(new CustomCsrfMiddleware()).start(Application.class, args);
    }

}
