package cool.develop.bingwallpaper.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.ByteBody;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.http.Session;
import com.blade.mvc.ui.RestResponse;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.exception.NotFoundException;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.dto.Resolution;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import cool.develop.bingwallpaper.service.SiteService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

import static cool.develop.bingwallpaper.bootstrap.BingWallpaperConst.COUNTRY;

/**
 * 首页、壁纸详情页
 *
 * @author vpday
 * @create 2018/11/23
 */
@Slf4j
@Path
public class IndexController {

    @Inject
    private BingWallpaperService bingWallpaperService;

    @Inject
    private SiteService siteService;

    /**
     * 首页
     */
    @GetRoute(value = {"/", "index", "index.html"})
    public String index(Request request) {
        return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, 1, 12);
    }

    /**
     * 首页分页
     */
    @GetRoute(value = {"page", "page/:page", "page/:page/:limit"})
    public String index(Request request, @PathParam(defaultValue = "1") int page, @PathParam(defaultValue = "12") int limit) {
        return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, page, limit);
    }

    /**
     * 热门榜
     */
    @GetRoute(value = {"ranking", "ranking/top", "ranking/top/:page", "ranking/top/:page/:limit"})
    public String topRanking(Request request, @PathParam(defaultValue = "1") int page, @PathParam(defaultValue = "12") int limit) {
        return this.toIndex(request, "/ranking/top", BingWallpaperConst.TOP_CODE, page, limit);
    }

    /**
     * 下载榜
     */
    @GetRoute(value = {"ranking/down", "ranking/down/:page", "ranking/down/:page/:limit"})
    public String downRanking(Request request, @PathParam(defaultValue = "1") int page, @PathParam(defaultValue = "12") int limit) {
        return this.toIndex(request, "/ranking/down", BingWallpaperConst.DOWN_CODE, page, limit);
    }

    private String toIndex(Request request, String pagePrefix, String pageType, Integer pageNum, Integer pageLimit) {
        CountryCode countryCode = CountryCode.getCountryCode(request.session().attribute(COUNTRY));
        return this.toIndex(request, pagePrefix, pageType, pageNum, pageLimit, countryCode);
    }

    private String toIndex(Request request, String pagePrefix, String pageType, Integer pageNum, Integer pageLimit, CountryCode country) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageLimit = pageLimit <= 0 ? 12 : pageLimit > 36 ? 12 : pageLimit;

        request.attribute("page_num", pageNum);
        request.attribute("page_limit", pageLimit);
        request.attribute("page_prefix", pagePrefix);
        request.attribute("page_type", pageType);

        request.attribute("country_code", country);

        return "index";
    }

    /**
     * 必应壁纸详情页
     */
    @GetRoute(value = {"details/:name", "details/:name/:lang"})
    public String details(Request request, @PathParam String name, @PathParam String lang) {
        if (StringKit.isEmpty(name)) {
            return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, 1, 12);
        }

        String codeStr = StringKit.isEmpty(lang) ? request.cookie(COUNTRY) : lang;
        CountryCode countryEnum = CountryCode.getCountryCode(codeStr);

        Optional<BingWallpaper> optionalObj = bingWallpaperService.getBingWallpaper(name, countryEnum);
        BingWallpaper bingWallpaper = optionalObj.orElseThrow(NotFoundException::new);

        bingWallpaperService.updateBingWallpaperByHits(name, lang, (bingWallpaper.getHits() + 1));
        request.attribute("wallPaper", bingWallpaper);

        return "details";
    }

    /**
     * 点击下载操作
     */
    @PostRoute(value = "download")
    public void downLoad(Request request, Response response) {
        Map<String, List<String>> query = request.parameters();

        String var = "code";
        if (null == query.get(var) || StringKit.isEmpty(query.get(var).get(0))) {
            response.json("{\"message\":\"request parameters are incomplete.\"}");
        } else {
            String code = query.get(var).get(0);
            BingWallpaper bingWallPaper = bingWallpaperService.getBingWallpaper(code);

            if (null == bingWallPaper) {
                response.json("{\"message\":\"the parameter code is incorrect.\"}");
            } else {
                bingWallpaperService.updateBingWallpaperByDownLoads(code, (bingWallPaper.getHits() + 1));

                File picture = bingWallpaperService.load(bingWallPaper.getName(),
                        new Resolution(1920, 1080));

                response.contentType("image/jpeg");
                response.header("Content-Disposition", "attachment; filename=" + picture.getName());
                response.body(ByteBody.of(picture));
            }
        }
    }

    /**
     * 点击喜欢操作
     */
    @JSON
    @PostRoute(value = "like")
    public RestResponse<?> likes(Request request) {
        Session session = request.session();
        Map<String, List<String>> query = request.parameters();

        String var1 = "code";
        if (null == query.get(var1) || StringKit.isEmpty(query.get(var1).get(0))) {
            return RestResponse.fail("请求参数不完整");
        }
        String code = query.get("code").get(0);
        BingWallpaper bingWallPaper = bingWallpaperService.getBingWallpaper(code);
        if (null == bingWallPaper) {
            return RestResponse.fail("参数 code 不正确");
        }

        Integer likes = bingWallPaper.getLikes();
        List<String> var = session.attribute("likes");
        if (null == var) {
            var = new ArrayList<>();
            session.attribute("likes", var);
        } else {
            long count = var.stream().filter(var2 -> var2.equals(code)).count();
            if (0 < count) {
                return RestResponse.ok(likes);
            }
        }
        var.add(code);

        likes += 1;
        bingWallpaperService.updateBingWallpaperByLikes(code, likes);

        return RestResponse.ok(likes);
    }

    /**
     * feed 页
     */
    @GetRoute(value = {"feed", "feed.xml", "feed/:code", "feed/:code.xml"})
    public void feed(Response response, @PathParam String code) {
        CountryCode country = CountryCode.getCountryCode(code);

        try {
            String xml = siteService.getRssXml(country);
            response.contentType("text/xml; charset=utf-8");
            response.body(xml);
        } catch (Exception e) {
            log.error("生成 rss 失败", e);
        }
    }

    /**
     * 设置国家编码
     */
    @GetRoute(value = {"country/:lang"})
    public String setCountry(Request request, Response response, @PathParam String lang) {
        CountryCode country = CountryCode.getCountryCode(lang);
        response.cookie(COUNTRY, country.code(), (60 * 60 * 24 * 30));

        return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, 1, 12, country);
    }
}
