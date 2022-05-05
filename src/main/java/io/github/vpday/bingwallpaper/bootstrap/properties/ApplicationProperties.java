package io.github.vpday.bingwallpaper.bootstrap.properties;

import com.hellokaton.blade.Environment;
import com.hellokaton.blade.mvc.BladeConst;
import io.github.vpday.bingwallpaper.utils.FileUtils;
import lombok.Data;

/**
 * 应用程序配置属性
 *
 * @author vpday
 * @date 2019/10/19
 */
@Data
public class ApplicationProperties {

    /**
     * 开发模式
     */
    private boolean devMode = true;

    /**
     * 站点访问地址，结尾无需斜杠（/）
     */
    private String siteUrl;

    /**
     * 站点标题
     */
    private String headTitle;

    /**
     * 本地存储壁纸的路径
     */
    private String bingWallpaperDir;

    public ApplicationProperties init(Environment environment) {
        ApplicationProperties applicationProperties = new ApplicationProperties();

        final String key1 = "app.dev";
        if (environment.hasKey(key1)) {
            boolean devMode1 = environment.getBoolean(key1, true);
            applicationProperties.setDevMode(devMode1);
        }
        final String key2 = "app.devMode";
        if (environment.hasKey(key2)) {
            boolean devMode2 = environment.getBoolean(key2, true);
            applicationProperties.setDevMode(devMode2);
        }

        String siteUrl1 = environment.get("app.site_url", "");
        applicationProperties.setSiteUrl(siteUrl1);

        String headTitle1 = environment.get("app.head_title", "");
        applicationProperties.setHeadTitle(headTitle1);

        String bingWallpaperDir1 = FileUtils.getFilePath((BladeConst.CLASSPATH + "wallpapers/"));
        applicationProperties.setBingWallpaperDir(bingWallpaperDir1);

        return applicationProperties;
    }
}
