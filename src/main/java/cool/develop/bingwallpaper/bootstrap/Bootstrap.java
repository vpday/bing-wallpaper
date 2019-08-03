package cool.develop.bingwallpaper.bootstrap;

import com.blade.Blade;
import com.blade.Environment;
import com.blade.ioc.Ioc;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.loader.BladeLoader;
import com.blade.mvc.Const;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import cool.develop.bingwallpaper.extension.Site;
import cool.develop.bingwallpaper.service.ServiceHandle;
import cool.develop.bingwallpaper.service.SiteService;
import cool.develop.bingwallpaper.utils.FileUtils;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Bean
@Slf4j
public class Bootstrap implements BladeLoader {

    @Inject
    private Environment environment;

    private static boolean DEV_MODE = Boolean.TRUE;

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
        BingWallpaperConst.SITE_URL = environment.get("app.site_url", "");

        templateEngine.getGlobalContext().set("context", BingWallpaperConst.SITE_URL);

        this.preAddData(blade.ioc());
        this.preConfigEmail();
    }

    /**
     * 预先添加数据
     */
    private void preAddData(Ioc ioc) {
        if (SqliteJdbc.IS_NEW_DB) {
            log.info("发现数据库为新创建，自动添加最近 15 天的必应壁纸信息");
            ioc.getBean(ServiceHandle.class).initDataBases();
        }
    }

    /**
     * 配置邮件发送
     */
    private void preConfigEmail() {
        boolean isEnable = environment.getBoolean("app.qq_email.enable", Boolean.FALSE);
        if (isEnable) {
            String username = environment.get("app.qq_email.username", "");
            String password = environment.get("app.qq_email.password", "");
            String toEmail = environment.get("app.email.to_email", "");

            isEnable = StringKit.isNotEmpty(username) && StringKit.isNotEmpty(password) && StringKit.isNotEmpty(toEmail);
            if (isEnable) {
                BingWallpaperConst.ENABLE_EMAIL = Boolean.TRUE;
                BingWallpaperConst.TO_EMAIL = toEmail;
                OhMyEmail.config(SMTP_QQ(false), username, password);
            }
        }
    }

    private boolean isDevMode(Blade blade) {
        if (blade.environment().hasKey("app.dev")) {
            DEV_MODE = blade.environment().getBoolean("app.dev", true);
        }
        if (blade.environment().hasKey("app.devMode")) {
            DEV_MODE = blade.environment().getBoolean("app.devMode", true);
        }

        return DEV_MODE;
    }

    public static boolean devMode() {
        return DEV_MODE;
    }
}
