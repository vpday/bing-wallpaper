package cool.develop.bingwallpaper.hooks;

import com.blade.kit.StringKit;
import com.blade.kit.UUID;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import com.blade.security.web.csrf.CsrfOption;
import cool.develop.bingwallpaper.utils.PasswordUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * @author vpday
 * @date 2019/4/13
 */
@Slf4j
@NoArgsConstructor
public class CustomCsrfMiddleware implements WebHook {
    private CsrfOption csrfOption = CsrfOption.builder().build();

    /**
     * Token stored in session
     */
    private static final String SESSION_TOKEN = "_csrf_token_session";

    public CustomCsrfMiddleware(CsrfOption csrfOption) {
        this.csrfOption = csrfOption;
    }

    @Override
    public boolean before(RouteContext context) {
        if (csrfOption.isIgnoreMethod(context.method())) {
            if (csrfOption.isStartExclusion(context.uri())) {
                return true;
            }
            this.genToken(context);
            return true;
        }

        if (csrfOption.isExclusion(context.uri())) {
            return true;
        }

        String tokenUUID = context.session().attribute(SESSION_TOKEN);
        if (StringKit.isEmpty(tokenUUID)) {
            csrfOption.getErrorHandler().accept(context);
            return false;
        }

        String token = csrfOption.getTokenGetter().apply(context.request());
        if (StringKit.isEmpty(token)) {
            csrfOption.getErrorHandler().accept(context);
            return false;
        }
        String hash = new String(Base64.getDecoder().decode(token));
        if (!PasswordUtils.checkPassword(tokenUUID, hash)) {
            csrfOption.getErrorHandler().accept(context);
            return false;
        }

        return true;
    }

    private void genToken(RouteContext context) {
        String tokenUUID = context.session().attribute(SESSION_TOKEN);
        if (StringKit.isEmpty(tokenUUID)) {
            tokenUUID = UUID.UU64();
            context.session().attribute(SESSION_TOKEN, tokenUUID);
        }
        String token = Base64.getEncoder().encodeToString(PasswordUtils.hashPassword(tokenUUID).getBytes());
        context.attribute("_csrf_token", token);
        context.attribute("_csrf_token_input", "<input type='hidden' name='_token' value='" + token + "'/>");
    }
}
