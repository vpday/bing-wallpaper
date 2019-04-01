package cool.develop.bingwallpaper.exception;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.WebContext;
import com.blade.mvc.handler.DefaultExceptionHandler;
import com.blade.mvc.http.Response;
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
        if (e instanceof NotFoundException) {
            Response response = WebContext.response();
            response.notFound();
            response.render("comm/error_404");
        } else if (e instanceof TipException) {
            // 发送邮件
            try {
                emailService.sendErrorInfo(SiteUtils.getStackTrace(e));
            } catch (SendMailException e1) {
                super.handle(e1);
            }
        } else {
            super.handle(e);
        }
    }
}
