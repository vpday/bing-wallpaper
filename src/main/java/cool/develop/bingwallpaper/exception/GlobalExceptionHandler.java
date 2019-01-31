package cool.develop.bingwallpaper.exception;

import com.blade.ioc.annotation.Bean;
import com.blade.mvc.WebContext;
import com.blade.mvc.handler.DefaultExceptionHandler;
import com.blade.mvc.http.Response;

/**
 * @author vpday
 * @create 2019/1/31
 */
@Bean
public class GlobalExceptionHandler extends DefaultExceptionHandler {

    @Override
    public void handle(Exception e) {
        if (e instanceof NotFoundException) {
            Response response = WebContext.response();
            response.notFound();
            response.render("comm/error_404");
        } else {
            super.handle(e);
        }
    }
}
