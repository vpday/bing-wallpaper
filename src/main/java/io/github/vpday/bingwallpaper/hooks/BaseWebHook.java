package io.github.vpday.bingwallpaper.hooks;

import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.kit.StringKit;
import com.hellokaton.blade.mvc.RouteContext;
import com.hellokaton.blade.mvc.hook.WebHook;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.utils.SiteUtils;

import static io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst.COUNTRY;

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
