package cool.develop.bingwallpaper.bootstrap;

import com.blade.Blade;
import com.blade.Environment;
import com.blade.ioc.Ioc;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.loader.BladeLoader;
import com.blade.mvc.Const;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.extension.Site;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import cool.develop.bingwallpaper.service.ServiceHandle;
import cool.develop.bingwallpaper.service.SiteService;
import cool.develop.bingwallpaper.utils.FileUtils;
import io.github.biezhi.anima.Anima;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Bean
@Slf4j
public class Bootstrap implements BladeLoader {

    @Inject
    private Environment environment;

    @Override
    public void preLoad(Blade blade) {
        Ioc ioc = blade.ioc();

        SqliteJdbc.importSql(this.isDevMode(blade));
        Anima.open(SqliteJdbc.DB_SRC);

        Site.setSiteService(ioc.getBean(SiteService.class));
    }

    @Override
    public void load(Blade blade) {
        JetbrickTemplateEngine templateEngine = new JetbrickTemplateEngine();
        templateEngine.getGlobalResolver().registerFunctions(Site.class);
        blade.templateEngine(templateEngine);

        String bingWallpaperDir = Const.CLASSPATH + "wallpapers/";
        BingWallpaperConst.BING_WALLPAPER_DIR = FileUtils.getFilePath(bingWallpaperDir);
        blade.addStatics("/wallpapers");

        BingWallpaperConst.HEAD_TITLE = environment.get("app.head_title", "");
        BingWallpaperConst.META_KEYWORDS = environment.get("app.meta_keywords", "");
        BingWallpaperConst.META_DESCRIPTION = environment.get("app.meta_description", "");
        BingWallpaperConst.SITE_URL = environment.get("app.site_url", "");

        this.preAddData(blade.ioc());
    }

    /**
     * 预先添加数据
     */
    private void preAddData(Ioc ioc) {
        try {
            if (SqliteJdbc.IS_NEW_DB) {
                log.info("发现数据库为新创建，自动添加最近 15 天的必应壁纸信息");
                ioc.getBean(ServiceHandle.class).saveBingWallpaperByFifteenDays();

            } else if (ioc.getBean(BingWallpaperService.class).isNotExistToDayWallpaper()) {
                log.info("数据库中未能查询到当天必应壁纸信息，自动添加当天的必应壁纸信息");
                // 程序启动时，判断数据库是否存在当天壁纸信息
                ioc.getBean(ServiceHandle.class).saveBingWallpaper();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new TipException(e.getMessage());
        }
    }

    private boolean isDevMode(Blade blade) {
        boolean devMode = true;
        if (blade.environment().hasKey("app.dev")) {
            devMode = blade.environment().getBoolean("app.dev", true);
        }
        if (blade.environment().hasKey("app.devMode")) {
            devMode = blade.environment().getBoolean("app.devMode", true);
        }

        return devMode;
    }
}
