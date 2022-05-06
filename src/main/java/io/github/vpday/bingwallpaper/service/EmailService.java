package io.github.vpday.bingwallpaper.service;

import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.mvc.WebContext;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import io.github.vpday.bingwallpaper.bootstrap.properties.QQEmailProperties;

/**
 * @author vpday
 * @date 2019/3/28
 */
@Bean
public class EmailService {

    @Inject
    private QQEmailProperties qqEmailProperties;

    /**
     * 邮件通知系统异常信息
     */
    public void sendErrorInfo(String text) throws SendMailException {
        if (qqEmailProperties.isEnable() && !WebContext.blade().devMode()) {
            OhMyEmail.subject("BingWallpaper_错误信息通知")
                    .from("BingWallpaper")
                    .to(qqEmailProperties.getAddressee())
                    .text(text).send();
        }
    }
}
