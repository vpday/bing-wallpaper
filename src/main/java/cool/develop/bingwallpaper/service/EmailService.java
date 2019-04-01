package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import cool.develop.bingwallpaper.bootstrap.Bootstrap;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;

import static cool.develop.bingwallpaper.bootstrap.BingWallpaperConst.ENABLE_EMAIL;
import static cool.develop.bingwallpaper.bootstrap.BingWallpaperConst.TO_EMAIL;

/**
 * @author vpday
 * @create 2019/3/28
 */
@Bean
public class EmailService {

    /**
     * 邮件通知系统异常信息
     */
    public void sendErrorInfo(String text) throws SendMailException {
        if (ENABLE_EMAIL && !Bootstrap.devMode()) {
            OhMyEmail.subject("BingWallpaper_错误信息通知")
                    .from("BingWallpaper").to(TO_EMAIL)
                    .text(text).send();
        }
    }
}
