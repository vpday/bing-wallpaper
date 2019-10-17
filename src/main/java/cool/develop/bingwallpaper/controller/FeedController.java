package cool.develop.bingwallpaper.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.http.Response;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.vo.Sitemap;
import cool.develop.bingwallpaper.service.SiteService;
import cool.develop.bingwallpaper.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author vpday
 * @create 2019/6/27
 */
@Slf4j
@Path
public class FeedController {

    private static final String XML_MEDIA_TYPE = "application/xml;charset=UTF-8";

    @Inject
    private SiteService siteService;

    /**
     * feed 页
     */
    @GetRoute(value = {"feed", "feed.xml", "feed/:code"})
    public void feed(Response response, @PathParam String code) {
        CountryCode country;

        // 去除 .xml 后缀
        String regex = "^.*\\.(xml)$";
        if (StringKit.isNotEmpty(code) && Pattern.matches(regex, code)) {
            String newCode = code.substring(0, code.lastIndexOf("."));

            country = CountryCode.getCountryCode(newCode);
        } else {
            country = CountryCode.getCountryCode(code);
        }

        try {
            String xml = siteService.getRssXml(country);
            response.contentType("text/xml; charset=utf-8");
            response.body(xml);
        } catch (Exception e) {
            log.error("生成 rss 失败", e);
        }
    }

    /**
     * Get sitemap.xml
     */
    @GetRoute(value = {"sitemap", "sitemap.xml", "sitemap/:code"})
    public void sitemapXml(Response response, @PathParam String code) {
        response.contentType(XML_MEDIA_TYPE);
        String templatePath = "comm/web/sitemap_xml.html";
        String newCode = CountryCode.ZH_CN.code();

        boolean isContinue;

        // 去除 .xml 后缀
        String regex = "^.*\\.(xml)$";
        isContinue = StringKit.isNotEmpty(code) && Pattern.matches(regex, code);
        if (isContinue) {
            newCode = code.substring(0, code.lastIndexOf("."));

            isContinue = CountryCode.isExistCode(newCode);
        }

        List<Sitemap> urlInfos;
        if (isContinue) {
            CountryCode country = CountryCode.getCountryCode(newCode);
            urlInfos = siteService.getSitemapUrls(country);
        } else {
            urlInfos = siteService.getSitemapUrls();
        }

        Map<String, Object> context = new ConcurrentHashMap<>(urlInfos.size());
        context.put("urls", urlInfos);
        SiteUtils.render(response, context, templatePath);
    }
}
