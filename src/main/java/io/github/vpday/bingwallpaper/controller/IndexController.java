package io.github.vpday.bingwallpaper.controller;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.exception.NotFoundException;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.kit.StringKit;
import com.hellokaton.blade.mvc.http.ByteBody;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.http.Session;
import com.hellokaton.blade.mvc.ui.RestResponse;
import io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst;
import io.github.vpday.bingwallpaper.model.entity.BingWallpaper;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.model.enums.ResolutionEnum;
import io.github.vpday.bingwallpaper.service.BingWallpaperService;
import io.github.vpday.bingwallpaper.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst.COUNTRY;

/**
 * 首页、壁纸详情页
 *
 * @author vpday
 * @date 2018/11/23
 */
@Slf4j
@Path
public class IndexController {

    @Inject
    private BingWallpaperService bingWallpaperService;

    private static final String CODE = "code";

    /**
     * 首页
     */
    @GET(value = {"/", "index", "index.html"})
    public String index(Request request) {
        return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, 1, 12);
    }

    /**
     * 首页分页
     */
    @GET(value = {"page", "page/:page", "page/:page/:limit"})
    public String index(Request request, @PathParam(defaultValue = "1") int page, @PathParam(defaultValue = "12") int limit) {
        return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, page, limit);
    }

    /**
     * 热门榜
     */
    @GET(value = {"ranking", "ranking/top", "ranking/top/:page", "ranking/top/:page/:limit"})
    public String topRanking(Request request, @PathParam(defaultValue = "1") int page, @PathParam(defaultValue = "12") int limit) {
        return this.toIndex(request, "/ranking/top", BingWallpaperConst.TOP_CODE, page, limit);
    }

    /**
     * 下载榜
     */
    @GET(value = {"ranking/down", "ranking/down/:page", "ranking/down/:page/:limit"})
    public String downRanking(Request request, @PathParam(defaultValue = "1") int page, @PathParam(defaultValue = "12") int limit) {
        return this.toIndex(request, "/ranking/down", BingWallpaperConst.DOWN_CODE, page, limit);
    }

    private String toIndex(Request request, String pagePrefix, String pageType, Integer pageNum, Integer pageLimit) {
        String displayName = SiteUtils.acceptLanguage(request).getDisplayName();
        CountryCodeEnum countryCodeEnum = CountryCodeEnum.getCountryCode(request.cookie(COUNTRY, displayName));
        return this.toIndex(request, pagePrefix, pageType, pageNum, pageLimit, countryCodeEnum);
    }

    private String toIndex(Request request, String pagePrefix, String pageType, Integer pageNum, Integer pageLimit, CountryCodeEnum country) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageLimit = pageLimit <= 0 ? 12 : pageLimit;
        pageLimit = pageLimit > 36 ? 12 : pageLimit;

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
    @GET(value = {"details/:name", "details/:name/:lang"})
    public String details(Request request, @PathParam String name, @PathParam String lang) {
        if (StringKit.isEmpty(name)) {
            return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, 1, 12);
        }

        CountryCodeEnum countryEnum = this.getCountryCode(request, lang);
        Optional<BingWallpaper> optionalObj = bingWallpaperService.getBingWallpaper(name, countryEnum);
        BingWallpaper bingWallpaper = optionalObj.orElseThrow(() -> {
            log.error("Not Found, name: [{}], countryCode: [{}].", name, countryEnum);
            return new NotFoundException("Not Found");
        });
        bingWallpaper.setHits(bingWallpaper.getHits() + 1);

        bingWallpaperService.updateBingWallpaperByHits(bingWallpaper.getHash(), bingWallpaper.getHits());
        request.attribute("wallPaper", bingWallpaper);

        return "details";
    }

    private CountryCodeEnum getCountryCode(Request request, String lang) {
        String codeStr;
        // 去除 .html 后缀
        String regex = "^.*\\.(html)$";
        if (StringKit.isNotEmpty(lang) && Pattern.matches(regex, lang)) {
            codeStr = lang.substring(0, lang.lastIndexOf('.'));
        } else {
            codeStr = StringKit.isEmpty(lang) ? request.cookie(COUNTRY) : lang;
        }
        return CountryCodeEnum.getCountryCode(codeStr);
    }

    /**
     * 点击下载操作
     */
    @POST(value = "download")
    public void downLoad(Request request, Response response) {
        RestResponse<String> checkResult = this.checkCode(request);
        if (!checkResult.isSuccess()) {
            response.json(checkResult);
            return;
        }
        RestResponse<BingWallpaper> result = this.getBingWallpaperByCode(checkResult.getPayload());
        if (!result.isSuccess()) {
            response.json(result);
            return;
        }

        BingWallpaper bingWallpaper = result.getPayload();
        bingWallpaperService.updateBingWallpaperByDownLoads(bingWallpaper.getHash(), (bingWallpaper.getHits() + 1));
        File picture = bingWallpaperService.load(bingWallpaper.getName(), ResolutionEnum.HD_1080P);
        response.contentType("image/jpeg");
        response.header("Content-Disposition", "attachment; filename=" + picture.getName());
        response.body(ByteBody.of(picture));
    }

    /**
     * 点击喜欢操作
     */
    @POST(value = "like")
    public RestResponse<?> likes(Request request) {
        RestResponse<String> checkResult = this.checkCode(request);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        RestResponse<BingWallpaper> result = this.getBingWallpaperByCode(checkResult.getPayload());
        if (!result.isSuccess()) {
            return result;
        }

        BingWallpaper bingWallpaper = result.getPayload();
        String wallPaperHash = bingWallpaper.getHash();
        Integer likes = bingWallpaper.getLikes();

        Session session = request.session();
        List<String> var = session.attribute("likes");
        if (Objects.isNull(var)) {
            var = new ArrayList<>();
            session.attribute("likes", var);
        } else {
            long count = var.stream().filter(var2 -> var2.equals(wallPaperHash)).count();
            if (0 < count) {
                return RestResponse.ok();
            }
        }
        var.add(wallPaperHash);

        likes += 1;
        bingWallpaperService.updateBingWallpaperByLikes(wallPaperHash, likes);

        return RestResponse.ok();
    }

    /**
     * 设置国家编码
     */
    @GET(value = {"country/:lang"})
    public String setCountry(Request request, Response response, @PathParam String lang) {
        CountryCodeEnum country = CountryCodeEnum.getCountryCode(lang);
        response.cookie(COUNTRY, country.code(), (60 * 60 * 24 * 30));
        return this.toIndex(request, "/page", BingWallpaperConst.INDEX_CODE, 1, 12, country);
    }

    private RestResponse<String> checkCode(Request request) {
        Map<String, List<String>> query = request.queries();
        if (Objects.isNull(query.get(CODE)) || StringKit.isEmpty(query.get(CODE).get(0))) {
            log.error("request parameters are incomplete.");
            return RestResponse.fail("request parameters are incomplete.");
        }
        return RestResponse.ok(query.get(CODE).get(0));
    }

    private RestResponse<BingWallpaper> getBingWallpaperByCode(String code) {
        BingWallpaper bingWallPaper = bingWallpaperService.getBingWallpaper(code);
        if (Objects.isNull(bingWallPaper)) {
            log.error("the parameter code [{}] is incorrect.", code);
            return RestResponse.fail("the parameter code is incorrect.");
        }
        return RestResponse.ok(bingWallPaper);
    }
}
