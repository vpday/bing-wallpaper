package cool.develop.bingwallpaper.extension;

import com.blade.mvc.WebContext;
import com.blade.mvc.http.Request;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.service.SiteService;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;
import jetbrick.template.runtime.InterpretContext;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 全站函数
 *
 * @author vpdy
 * @create 2018/11/23
 */
public final class Site {
    private static SiteService siteService;

    public static void setSiteService(SiteService ss) {
        siteService = ss;
    }

    public static int getYear() {
        return LocalDate.now().getYear();
    }

    /**
     * 获取页面标题
     */
    public static String headTitle(String hashCode) {
        String pageType = getRequestAttribute("page_type");
        Integer pageNum = getRequestAttribute("page_num");

        if (null != pageType && null != pageNum) {
            String text = BingWallpaperConst.HEAD_TITLE;

            if (BingWallpaperConst.TOP_CODE.equals(pageType)) {
                text = "热门榜 | " + BingWallpaperConst.HEAD_TITLE;
            } else if (BingWallpaperConst.DOWN_CODE.equals(pageType)) {
                text = "下载榜 | " + BingWallpaperConst.HEAD_TITLE;
            }

            return text + " | 第" + pageNum + "页";
        }

        String title;
        BingWallpaper bingWallPaper = currentBingWallPaper();
        if (Objects.nonNull(bingWallPaper)) {
            title = bingWallPaper.getTitle() + " | " + bingWallPaper.getCaption();
        } else {
            title = siteService.getTitle(hashCode);
        }

        return title + " | " + BingWallpaperConst.HEAD_TITLE;
    }

    /**
     * 获取页面关键字
     */
    public static String metaKeywords(String hashCode) {
        String pageType = getRequestAttribute("page_type");

        if (null != pageType && pageType.equals(hashCode)) {
            return BingWallpaperConst.META_KEYWORDS;
        }

        BingWallpaper bingWallPaper = currentBingWallPaper();
        if (Objects.nonNull(bingWallPaper)) {
            return bingWallPaper.getKeywords();
        } else {
            return siteService.getKeywords(hashCode);
        }
    }

    /**
     * 获取页面描述
     */
    public static String metaDescription(String hashCode) {
        String pageType = getRequestAttribute("page_type");

        if (null != pageType && pageType.equals(hashCode)) {
            return BingWallpaperConst.META_DESCRIPTION;
        }

        BingWallpaper bingWallPaper = currentBingWallPaper();
        if (Objects.nonNull(bingWallPaper)) {
            return bingWallPaper.getDescription();
        } else {
            return siteService.getDescription(hashCode);
        }
    }

    /**
     * 获取页面创作者
     */
    public static String metaAuthor(String hashCode) {
        String pageType = getRequestAttribute("page_type");

        if (null != pageType && pageType.equals(hashCode)) {
            return BingWallpaperConst.META_AUTHOR;
        }

        BingWallpaper bingWallPaper = currentBingWallPaper();
        if (Objects.nonNull(bingWallPaper)) {
            return bingWallPaper.getCopyright();
        } else {
            return siteService.getCopyright(hashCode);
        }
    }

    /**
     * 获取全部语言编码
     */
    public static CountryCode[] getAllCountry() {
        return CountryCode.values();
    }

    /**
     * 获取图片全称
     */
    public static String getFileName(BingWallpaper bingWallPaper) {
        return bingWallPaper.getName() + ".jpg";
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
        return "/details/" + bingWallPaper.getName();
    }

    /**
     * 获取归属地
     */
    public static String attribute(BingWallpaper bingWallPaper) {
        return siteService.getAttribute(bingWallPaper.getName());
    }

    /**
     * 获取 Google Map Url
     */
    public static String mapUrl(BingWallpaper bingWallPaper) {
        return siteService.getMapUrl(bingWallPaper.getName());
    }

    public static String unixTimeToString(long unixTime) {
        SimpleDateFormat format = new SimpleDateFormat(BingWallpaperConst.DATE_PATTERN);
        Date date = Date.from(Instant.ofEpochMilli(unixTime));
        return format.format(date);
    }

    /**
     * 分页
     */
    public static Page<BingWallpaper> wallPapers() {
        Request request = WebContext.request();

        Optional<Integer> optionalPage = Optional.of(request.attribute("page_num"));
        Integer page = optionalPage.orElse(1);

        Optional<Integer> optionalLimit = Optional.of(request.attribute("page_limit"));
        Integer limit = optionalLimit.orElse(1);

        Optional<String> optionalType = Optional.of(request.attribute("page_type"));
        String type = optionalType.orElse("index");

        Optional<CountryCode> optionalCountry = Optional.of(request.attribute("country_code"));
        CountryCode country = optionalCountry.orElse(CountryCode.ZH_CN);

        Page<BingWallpaper> wallPapers = getPaging(page, limit, type, country);
        request.attribute("wallPapers", wallPapers);

        return wallPapers;
    }

    /**
     * 构建分页查询
     */
    private static Page<BingWallpaper> getPaging(Integer page, Integer limit, String type, CountryCode country) {
        AnimaQuery<BingWallpaper> query = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCountry, country.code());

        if (BingWallpaperConst.TOP_CODE.equals(type)) {
            query.order(BingWallpaper::getLikes, OrderBy.DESC);
        } else if (BingWallpaperConst.DOWN_CODE.equals(type)) {
            query.order(BingWallpaper::getDownloads, OrderBy.DESC);
        } else {
            query.order(BingWallpaper::getDate, OrderBy.DESC);
        }

        return query.page(page, limit);
    }

    /**
     * 获取当前上下文的必应壁纸对象
     */
    public static BingWallpaper currentBingWallPaper() {
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("wallPaper");

        return null == value ? null : (BingWallpaper) value;
    }

    private static <T> T getRequestAttribute(String name) {
        return WebContext.request().attribute(name);
    }
}
