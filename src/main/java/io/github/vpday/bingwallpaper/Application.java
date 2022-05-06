package io.github.vpday.bingwallpaper;

import com.hellokaton.blade.Blade;
import io.github.vpday.bingwallpaper.bootstrap.ApplicationLoader;

/**
 * @author vpday
 * @date 2018/11/23
 */
public class Application {

    public static void main(String[] args) {
        Blade blade = Blade.create();
        ApplicationLoader.init(blade);
        blade.start(Application.class, args);
    }

}
