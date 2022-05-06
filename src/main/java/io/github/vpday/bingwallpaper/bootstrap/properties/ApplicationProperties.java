package io.github.vpday.bingwallpaper.bootstrap.properties;

import com.hellokaton.blade.Environment;
import com.hellokaton.blade.mvc.BladeConst;
import io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst;
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

        String siteUrl1 = environment.get("app.site_url", "");
        applicationProperties.setSiteUrl(siteUrl1);

        String headTitle1 = environment.get("app.head_title", "");
        applicationProperties.setHeadTitle(headTitle1);

        String bingWallpaperDir1 = FileUtils.getFilePath((BladeConst.CLASSPATH + BingWallpaperConst.WALLPAPERS_PATH));
        applicationProperties.setBingWallpaperDir(bingWallpaperDir1);

        return applicationProperties;
    }
}
