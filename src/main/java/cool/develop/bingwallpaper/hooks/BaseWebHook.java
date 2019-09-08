package cool.develop.bingwallpaper.hooks;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;

import static cool.develop.bingwallpaper.bootstrap.BingWallpaperConst.COUNTRY;

/**
 * @author vpday
 * @create 2019/1/24
 */
@Bean
@Slf4j
public class BaseWebHook implements WebHook {
    @Override
    public boolean before(RouteContext context) {

        String langByCookie = context.cookie(COUNTRY);
        boolean isNotExist = StringKit.isEmpty(langByCookie) || CountryCode.isNotExistCode(langByCookie);
        if (isNotExist) {
            String displayName = SiteUtils.acceptLanguage(context.request()).getDisplayName();
            context.cookie(COUNTRY, displayName, 2592000);
        }

        return true;
    }
}
