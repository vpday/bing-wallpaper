package cool.develop.bingwallpaper.bootstrap;

import com.blade.Blade;
import com.blade.Environment;
import com.blade.ioc.Ioc;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.loader.BladeLoader;
import com.blade.mvc.Const;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import cool.develop.bingwallpaper.extension.Site;
import cool.develop.bingwallpaper.service.BingService;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import cool.develop.bingwallpaper.service.SiteService;
import cool.develop.bingwallpaper.utils.SiteUtils;
import io.github.biezhi.anima.Anima;
import lombok.extern.slf4j.Slf4j;

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

        boolean devMode = true;
        if (blade.environment().hasKey("app.dev")) {
            devMode = blade.environment().getBoolean("app.dev", true);
        }
        if (blade.environment().hasKey("app.devMode")) {
            devMode = blade.environment().getBoolean("app.devMode", true);
        }
        SqliteJdbc.importSql(devMode);
        Anima.open(SqliteJdbc.DB_SRC);

        Site.setSiteService(ioc.getBean(SiteService.class));
    }

    @Override
    public void load(Blade blade) {
        JetbrickTemplateEngine templateEngine = new JetbrickTemplateEngine();
        templateEngine.getGlobalResolver().registerFunctions(Site.class);
        blade.templateEngine(templateEngine);

        String bingWallpaperDir = Const.CLASSPATH + "wallpapers/";
        BingWallpaperConst.BING_WALLPAPER_DIR = SiteUtils.getFilePath(bingWallpaperDir);
        blade.addStatics("/wallpapers");

        BingWallpaperConst.HEAD_TITLE = environment.get("app.head_title", "");
        BingWallpaperConst.META_KEYWORDS = environment.get("app.meta_keywords", "");
        BingWallpaperConst.META_DESCRIPTION = environment.get("app.meta_description", "");
        BingWallpaperConst.SITE_URL = environment.get("app.site_url", "");

        Ioc ioc = blade.ioc();
        BingWallpaperService bingWallpaperService = ioc.getBean(BingWallpaperService.class);

        // 程序启动时，判断数据库是否存在当天壁纸信息
        if (bingWallpaperService.isNotExistToDayWallpaper()) {
            BingService bingService = ioc.getBean(BingService.class);

            SiteUtils.saveCoverStoryAndImageArchive(bingService, bingWallpaperService);
        }
    }
}
