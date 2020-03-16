package cool.develop.bingwallpaper.hooks;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import cool.develop.bingwallpaper.model.enums.CountryCodeEnum;
import cool.develop.bingwallpaper.utils.SiteUtils;

import static cool.develop.bingwallpaper.bootstrap.BingWallpaperConst.COUNTRY;

/**
 * @author vpday
 * @date 2019/1/24
 */
@Bean
public class BaseWebHook implements WebHook {
    @Override
    public boolean before(RouteContext context) {

        String langByCookie = context.cookie(COUNTRY);
        boolean isNotExist = StringKit.isEmpty(langByCookie) || CountryCodeEnum.isNotExistCode(langByCookie);
        if (isNotExist) {
            String displayName = SiteUtils.acceptLanguage(context.request()).getDisplayName();
            CountryCodeEnum countryCodeEnum = CountryCodeEnum.getCountryCode(displayName);
            context.cookie(COUNTRY, countryCodeEnum.code(), 2592000);
        }

        return true;
    }
}
