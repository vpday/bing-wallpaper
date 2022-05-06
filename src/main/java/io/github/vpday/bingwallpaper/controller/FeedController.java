package io.github.vpday.bingwallpaper.controller;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.kit.StringKit;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.template.JetbrickTemplateEngine;
import io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst;
import io.github.vpday.bingwallpaper.bootstrap.properties.ApplicationProperties;
import io.github.vpday.bingwallpaper.model.entity.BingWallpaper;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.model.vo.Sitemap;
import io.github.vpday.bingwallpaper.service.BingWallpaperService;
import io.github.vpday.bingwallpaper.service.FeedService;
import io.github.vpday.bingwallpaper.utils.JetbrickTemplateUtils;
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

    private static final int LIMIT_CONTENT = 15;

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
    @GET(value = {"feed", "feed.xml", "feed/:code"})
    public void feed(Response response, @PathParam String code) {
        CountryCodeEnum country;

        // 去除 .xml 后缀
        String regex = "^.*\\.(xml)$";
        if (StringKit.isNotEmpty(code) && Pattern.matches(regex, code)) {
            String newCode = code.substring(0, code.lastIndexOf('.'));
            country = CountryCodeEnum.getCountryCode(newCode);
        } else {
            country = CountryCodeEnum.getCountryCode(code);
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
    @GET(value = {"sitemap", "sitemap.xml", "sitemap/:code"})
    public void sitemapXml(Response response, @PathParam String code) {
        String newCode = CountryCodeEnum.ZH_CN.code();
        boolean isContinue;

        // 去除 .xml 后缀
        String regex = "^.*\\.(xml)$";
        isContinue = StringKit.isNotEmpty(code) && Pattern.matches(regex, code);
        if (isContinue) {
            newCode = code.substring(0, code.lastIndexOf('.'));
            isContinue = CountryCodeEnum.isExistCode(newCode);
        }

        List<Sitemap> urlInfoList;
        if (isContinue) {
            CountryCodeEnum country = CountryCodeEnum.getCountryCode(newCode);
            List<BingWallpaper> bingWallpapers = bingWallpaperService.listAllBy(country, LIMIT_CONTENT);
            urlInfoList = feedService.listAllSitemapUrls(country, bingWallpapers);
        } else {
            urlInfoList = feedService.listAllSitemapUrls();
        }

        Map<String, Object> context = new ConcurrentHashMap<>(16);
        context.put("urls", urlInfoList);

        JetTemplate jetTemplate = jetbrickTemplateEngine.getJetEngine().getTemplate("comm/web/sitemap_xml.html");
        String xmlBody = JetbrickTemplateUtils.processTemplateIntoString(jetTemplate, context);
        this.responseXml(response, xmlBody);
    }

    private void responseXml(Response response, String xmlBody) {
        response.contentType(BingWallpaperConst.XML_MEDIA_TYPE);
        response.body(xmlBody);
    }
}
