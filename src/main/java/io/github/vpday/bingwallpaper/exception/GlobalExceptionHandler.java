package io.github.vpday.bingwallpaper.exception;

import com.hellokaton.blade.exception.BladeException;
import com.hellokaton.blade.exception.NotFoundException;
import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.kit.JsonKit;
import com.hellokaton.blade.mvc.WebContext;
import com.hellokaton.blade.mvc.handler.DefaultExceptionHandler;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.ui.RestResponse;
import io.github.vpday.bingwallpaper.service.EmailService;
import io.github.vpday.bingwallpaper.utils.SiteUtils;
import io.github.biezhi.ome.SendMailException;

/**
 * @author vpday
 * @date 2019/1/31
 */
@Bean
public class GlobalExceptionHandler extends DefaultExceptionHandler {

    @Inject
    private EmailService emailService;

    @Override
    public void handle(Exception e) {
        Request request = WebContext.request();
        Response response = WebContext.response();
        boolean isNotFound = e instanceof BladeException && ((BladeException) e).getStatus() == NotFoundException.STATUS;
        if (isNotFound) {
            if (request.isJsonRequest()) {
                response.json(RestResponse.fail(NotFoundException.STATUS, "Not Found [" + request.uri() + "]"));
            } else {
                response.notFound().render("comm/error_404");
            }
        } else {
            // 发送邮件
            try {
                String errorInfo = String.format("method: [%s]%nurl: [%s]%nparameters: [%s]%nuserAgent: [%s]%n%n",
                        request.method(),
                        request.url(),
                        JsonKit.toString(request.queries()),
                        request.userAgent());

                emailService.sendErrorInfo((errorInfo + SiteUtils.getStackTrace(e)));
                super.handle(e);
            } catch (SendMailException e1) {
                super.handle(e1);
            }
        }
    }
}
