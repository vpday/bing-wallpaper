package io.github.vpday.bingwallpaper.extension;

import com.hellokaton.anima.Anima;
import com.hellokaton.anima.core.AnimaQuery;
import com.hellokaton.anima.core.JoinParam;
import com.hellokaton.anima.core.Joins;
import com.hellokaton.anima.enums.OrderBy;
import com.hellokaton.anima.page.Page;
import com.hellokaton.blade.mvc.WebContext;
import com.hellokaton.blade.mvc.http.Request;
import io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst;
import io.github.vpday.bingwallpaper.model.entity.BingWallpaper;
import io.github.vpday.bingwallpaper.model.entity.FilmingLocation;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.model.enums.ResolutionEnum;
import io.github.vpday.bingwallpaper.utils.DateUtils;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import static io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst.COUNTRY;

/**
 * 全站函数
 *
 * @author vpdy
 * @date 2018/11/23
 */
public final class Site {

    private static String headTitle;

    private Site() {
    }

    public static void setHeadTitle(String setHeadTitle) {
        headTitle = setHeadTitle;
    }

    public static String siteName() {
        return headTitle;
    }

    public static String wallpaperLocale() {
        return CountryCodeEnum.getCountryCode(request().cookie(COUNTRY)).code();
    }

    /**
     * 获取页面标题
     */
    public static String headTitle(BingWallpaper wallpaper) {
        if (Objects.isNull(wallpaper)) {
            final String pageType = getRequestAttribute("page_type");
            final Integer pageNum = getRequestAttribute("page_num");
            String text = headTitle;

            if (BingWallpaperConst.TOP_CODE.equals(pageType)) {
                text = "热门榜 | " + headTitle;
            } else if (BingWallpaperConst.DOWN_CODE.equals(pageType)) {
                text = "下载榜 | " + headTitle;
            }

            if (1 == pageNum) {
                return text;
            }
            return text + " | 第 " + pageNum + " 页";
        } else {
            return wallpaper.getTitle() + " | " + headTitle;
        }
    }

    /**
     * 获取全部语言编码
     */
    public static CountryCodeEnum[] getAllCountry() {
        return CountryCodeEnum.values();
    }

    /**
     * 获取图片全称
     */
    public static String getFileName(BingWallpaper bingWallPaper) {
        return bingWallPaper.getName() + ".jpg";
    }

    /**
     * 获取高清图片的访问 URL
     */
    public static String imgHrefByHD(BingWallpaper bingWallPaper) {
        return imgHref(bingWallPaper, ResolutionEnum.HD_1080P.format());
    }

    /**
     * 获取图片的访问 URL
     */
    public static String imgHrefByMobileWXGA(BingWallpaper bingWallPaper) {
        return imgHref(bingWallPaper, ResolutionEnum.MOBILE_WXGA_360P.format());
    }

    /**
     * 获取图片的访问 URL
     */
    public static String imgHref(BingWallpaper bingWallPaper, String resolution) {
        String name = bingWallPaper.getName();
        return "/wallpapers/" + name + "/" + name + "_" + resolution + ".jpg";
    }

    /**
     * 获取壁纸详情页面的 URL
     */
    public static String detailsHref(BingWallpaper bingWallPaper) {
        return String.format("/details/%s/%s.html", bingWallPaper.getName(), bingWallPaper.getCountry().code());
    }

    /**
     * 获取壁纸详情页面的 URL
     */
    public static String detailsDefaultHref(BingWallpaper bingWallPaper) {
        return "/details/" + bingWallPaper.getName();
    }

    /**
     * 获取归属地
     */
    public static String attribute(BingWallpaper bingWallPaper) {
        if (!Objects.isNull(bingWallPaper.getFilmingLocation())) {
            return bingWallPaper.getFilmingLocation().getAttribute();
        }

        return "";
    }

    /**
     * 获取 Google Map Url
     */
    public static String mapUrl(BingWallpaper bingWallPaper) {
        if (!Objects.isNull(bingWallPaper.getFilmingLocation())) {
            return bingWallPaper.getFilmingLocation().getMapUrl();
        }

        return "";
    }

    public static String unixTimeToString(long unixTime) {
        return DateUtils.epochMilliToLocalDate(unixTime).format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 分页
     */
    public static Page<BingWallpaper> paging() {
        Request request = WebContext.request();

        Optional<Integer> optionalPage = Optional.of(request.attribute("page_num"));
        Integer page = optionalPage.orElse(1);

        Optional<Integer> optionalLimit = Optional.of(request.attribute("page_limit"));
        Integer limit = optionalLimit.orElse(1);

        Optional<String> optionalType = Optional.of(request.attribute("page_type"));
        String type = optionalType.orElse("index");

        Optional<CountryCodeEnum> optionalCountry = Optional.of(request.attribute("country_code"));
        CountryCodeEnum country = optionalCountry.orElse(CountryCodeEnum.ZH_CN);

        Page<BingWallpaper> wallPapers = getPaging(page, limit, type, country);
        request.attribute("wallPapers", wallPapers);

        return wallPapers;
    }

    /**
     * 构建分页查询
     */
    private static Page<BingWallpaper> getPaging(Integer page, Integer limit, String type, CountryCodeEnum country) {
        JoinParam param = Joins.with(FilmingLocation.class).as(BingWallpaper::getFilmingLocation)
                .on(BingWallpaper::getName, FilmingLocation::getName);
        param.setFieldName("filmingLocation");

        AnimaQuery<BingWallpaper> query = Anima.select().from(BingWallpaper.class)
                .join(param).where(BingWallpaper::getCountry, country.code());

        if (BingWallpaperConst.TOP_CODE.equals(type)) {
            query.order(BingWallpaper::getLikes, OrderBy.DESC);
        } else if (BingWallpaperConst.DOWN_CODE.equals(type)) {
            query.order(BingWallpaper::getDownloads, OrderBy.DESC);
        } else {
            query.order(BingWallpaper::getDate, OrderBy.DESC);
        }

        return query.page(page, limit);
    }

    private static <T> T getRequestAttribute(String name) {
        return request().attribute(name);
    }

    private static Request request() {
        return WebContext.request();
    }
}
