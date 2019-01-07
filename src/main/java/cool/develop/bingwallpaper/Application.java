package cool.develop.bingwallpaper;

import com.blade.Blade;
import com.blade.security.web.csrf.CsrfMiddleware;
import cool.develop.bingwallpaper.bootstrap.ApplicationLoader;

/**
 * @author vpday
 * @create 2018/11/23
 */
public class Application {

    public static void main(String[] args) {
        Blade blade = Blade.of();
        ApplicationLoader.init(blade);
        blade.use(new CsrfMiddleware()).start(Application.class, args);
    }

}
