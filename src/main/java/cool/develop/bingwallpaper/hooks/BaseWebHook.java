package cool.develop.bingwallpaper.hooks;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vpday
 * @create 2019/1/24
 */
@Bean
@Slf4j
public class BaseWebHook implements WebHook {
    @Override
    public boolean before(RouteContext context) {

        String lang = context.cookie(BingWallpaperConst.COUNTRY);
        if (StringKit.isEmpty(lang) || CountryCode.isNotExistCode(lang)) {
            context.cookie(BingWallpaperConst.COUNTRY, CountryCode.ZH_CN.code(), 2592000);
        }

        return true;
    }
}
