package cool.develop.bingwallpaper.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.http.Response;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.bootstrap.properties.ApplicationProperties;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.model.enums.CountryCode;
import cool.develop.bingwallpaper.model.vo.Sitemap;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import cool.develop.bingwallpaper.service.FeedService;
import cool.develop.bingwallpaper.utils.JetbrickTemplateUtils;
import jetbrick.template.JetTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author vpday
 * @date 2019/6/27
 */
@Slf4j
@Path
public class FeedController {

    private static final int LIMIT_CONTENT = Integer.MAX_VALUE;

    @Inject
    private JetbrickTemplateEngine jetbrickTemplateEngine;

    @Inject
    private ApplicationProperties applicationProperties;

    @Inject
    private FeedService feedService;

    @Inject
    private BingWallpaperService bingWallpaperService;

    /**
     * feed 页
     */
    @GetRoute(value = {"feed", "feed.xml", "feed/:code"})
    public void feed(Response response, @PathParam String code) {
        CountryCode country;

        // 去除 .xml 后缀
        String regex = "^.*\\.(xml)$";
        if (StringKit.isNotEmpty(code) && Pattern.matches(regex, code)) {
            String newCode = code.substring(0, code.lastIndexOf('.'));
            country = CountryCode.getCountryCode(newCode);
        } else {
            country = CountryCode.getCountryCode(code);
        }

        List<BingWallpaper> bingWallpapers = bingWallpaperService.listAllBy(country, LIMIT_CONTENT);
        Map<String, Object> context = new ConcurrentHashMap<>(16);
        context.put("copyright", BingWallpaperConst.META_AUTHOR);
        context.put("countryCode", country);
        context.put("bingWallpapers", bingWallpapers);
        context.put("applicationProperties", applicationProperties);

        JetTemplate jetTemplate = jetbrickTemplateEngine.getJetEngine().getTemplate("comm/web/rss.html");
        String xmlBody = JetbrickTemplateUtils.processTemplateIntoString(jetTemplate, context);
        this.responseXml(response, xmlBody);
    }

    /**
     * Get sitemap.xml
     */
    @GetRoute(value = {"sitemap", "sitemap.xml", "sitemap/:code"})
    public void sitemapXml(Response response, @PathParam String code) {
        String newCode = CountryCode.ZH_CN.code();
        boolean isContinue;

        // 去除 .xml 后缀
        String regex = "^.*\\.(xml)$";
        isContinue = StringKit.isNotEmpty(code) && Pattern.matches(regex, code);
        if (isContinue) {
            newCode = code.substring(0, code.lastIndexOf('.'));
            isContinue = CountryCode.isExistCode(newCode);
        }

        List<Sitemap> urlInfos;
        if (isContinue) {
            CountryCode country = CountryCode.getCountryCode(newCode);
            List<BingWallpaper> bingWallpapers = bingWallpaperService.listAllBy(country, LIMIT_CONTENT);
            urlInfos = feedService.listAllSitemapUrls(country, bingWallpapers);
        } else {
            urlInfos = feedService.listAllSitemapUrls();
        }

        Map<String, Object> context = new ConcurrentHashMap<>(16);
        context.put("urls", urlInfos);

        JetTemplate jetTemplate = jetbrickTemplateEngine.getJetEngine().getTemplate("comm/web/sitemap_xml.html");
        String xmlBody = JetbrickTemplateUtils.processTemplateIntoString(jetTemplate, context);
        this.responseXml(response, xmlBody);
    }

    private void responseXml(Response response, String xmlBody) {
        response.contentType(BingWallpaperConst.XML_MEDIA_TYPE);
        response.body(xmlBody);
    }
}
