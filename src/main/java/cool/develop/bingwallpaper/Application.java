package cool.develop.bingwallpaper;

import com.blade.Blade;
import com.blade.security.web.csrf.CsrfMiddleware;

/**
 * @author vpday
 * @create 2018/11/23
 */
public class Application {

    public static void main(String[] args) {
        Blade blade = Blade.of();
        blade.use(new CsrfMiddleware()).start(Application.class, args);
    }

}
