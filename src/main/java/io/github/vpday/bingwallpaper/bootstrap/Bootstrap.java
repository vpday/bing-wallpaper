package io.github.vpday.bingwallpaper.bootstrap;

import com.hellokaton.anima.Anima;
import com.hellokaton.blade.Blade;
import com.hellokaton.blade.Environment;
import com.hellokaton.blade.ioc.Ioc;
import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.kit.StringKit;
import com.hellokaton.blade.loader.BladeLoader;
import com.hellokaton.blade.template.JetbrickTemplateEngine;
import io.github.biezhi.ome.OhMyEmail;
import io.github.vpday.bingwallpaper.bootstrap.properties.ApplicationProperties;
import io.github.vpday.bingwallpaper.bootstrap.properties.QQEmailProperties;
import io.github.vpday.bingwallpaper.exception.TipException;
import io.github.vpday.bingwallpaper.extension.Site;
import io.github.vpday.bingwallpaper.service.ServiceHandle;
import jetbrick.template.JetEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

/**
 * @author vpday
 * @date 2018/11/23
 */
@Bean
@Slf4j
public class Bootstrap implements BladeLoader {

    private ApplicationProperties applicationProperties;

    private QQEmailProperties qqEmailProperties;

    private JetbrickTemplateEngine jetbrickTemplateEngine;

    @Override
    public void preLoad(Blade blade) {
        this.preInitBean(blade.environment(), blade.ioc());

        jetbrickTemplateEngine = new JetbrickTemplateEngine();
        jetbrickTemplateEngine.getGlobalResolver().registerFunctions(Site.class);
        jetbrickTemplateEngine.getGlobalContext().set("context", applicationProperties.getSiteUrl());
        if (Objects.isNull(jetbrickTemplateEngine.getJetEngine())) {
            jetbrickTemplateEngine.setJetEngine(JetEngine.create(jetbrickTemplateEngine.getConfig()));
        }
        blade.ioc().addBean(jetbrickTemplateEngine);

        SqliteJdbc.importSql();
        Anima.open(SqliteJdbc.dbSrc());

        Site.setHeadTitle(applicationProperties.getHeadTitle());
    }

    @Override
    public void load(Blade blade) {
        blade.templateEngine(jetbrickTemplateEngine);

        this.preAddData(blade.ioc());
        this.preConfigEmail();
    }

    /**
     * 初始化配置属性
     *
     * @param environment Blade 环境配置
     * @param ioc         IOC 容器
     */
    private void preInitBean(Environment environment, Ioc ioc) {
        this.applicationProperties = new ApplicationProperties().init(environment);
        ioc.addBean(this.applicationProperties);
        this.qqEmailProperties = new QQEmailProperties().init(environment);
        ioc.addBean(this.qqEmailProperties);
    }

    /**
     * 预先添加数据
     *
     * @param ioc IOC 容器
     */
    private void preAddData(Ioc ioc) {
        if (Boolean.TRUE.equals(SqliteJdbc.isNewDb())) {
            log.info("发现数据库为新创建，自动添加最近 15 天的必应壁纸信息");
            ioc.getBean(ServiceHandle.class).initDataBases();
        }
    }

    /**
     * 配置邮件发送
     */
    private void preConfigEmail() {
        if (qqEmailProperties.isEnable()) {
            final String username = qqEmailProperties.getUsername();
            final String password = qqEmailProperties.getPassword();
            final String addressee = qqEmailProperties.getAddressee();

            if (StringKit.isBlank(username)) {
                throw new TipException("邮件登录用户名不能为空");
            }
            if (StringKit.isBlank(password)) {
                throw new TipException("邮件登录密码不能为空");
            }
            if (StringKit.isBlank(addressee)) {
                throw new TipException("收件人不能为空");
            }

            OhMyEmail.config(SMTP_QQ(false), username, password);
        }
    }
}
