package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import cool.develop.bingwallpaper.bootstrap.properties.ApplicationProperties;
import cool.develop.bingwallpaper.bootstrap.properties.QQEmailProperties;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;

/**
 * @author vpday
 * @date 2019/3/28
 */
@Bean
public class EmailService {

    @Inject
    private QQEmailProperties qqEmailProperties;

    @Inject
    private ApplicationProperties applicationProperties;

    /**
     * 邮件通知系统异常信息
     */
    public void sendErrorInfo(String text) throws SendMailException {
        if (qqEmailProperties.isEnable() && !applicationProperties.isDevMode()) {
            OhMyEmail.subject("BingWallpaper_错误信息通知")
                    .from("BingWallpaper").to(qqEmailProperties.getAddressee())
                    .text(text).send();
        }
    }
}
