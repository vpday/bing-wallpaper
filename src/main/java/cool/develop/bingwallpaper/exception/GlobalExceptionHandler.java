package cool.develop.bingwallpaper.exception;

import com.blade.exception.BladeException;
import com.blade.exception.NotFoundException;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.JsonKit;
import com.blade.mvc.WebContext;
import com.blade.mvc.handler.DefaultExceptionHandler;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.ui.RestResponse;
import cool.develop.bingwallpaper.service.EmailService;
import cool.develop.bingwallpaper.utils.SiteUtils;
import io.github.biezhi.ome.SendMailException;

/**
 * @author vpday
 * @create 2019/1/31
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
                String errorInfo = String.format("method: [%s]\nurl: [%s]\nparameters: [%s]\nuserAgent: [%s]\n\n",
                        request.method(),
                        request.url(),
                        JsonKit.toString(request.parameters()),
                        request.userAgent());

                emailService.sendErrorInfo((errorInfo + SiteUtils.getStackTrace(e)));
                super.handle(e);
            } catch (SendMailException e1) {
                super.handle(e1);
            }
        }
    }
}
