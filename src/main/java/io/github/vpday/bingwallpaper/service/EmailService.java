package io.github.vpday.bingwallpaper.service;

import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.ioc.annotation.Inject;
import io.github.vpday.bingwallpaper.bootstrap.properties.ApplicationProperties;
import io.github.vpday.bingwallpaper.bootstrap.properties.QQEmailProperties;
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
