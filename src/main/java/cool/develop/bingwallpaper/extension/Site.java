package cool.develop.bingwallpaper.extension;

import com.blade.mvc.WebContext;
import com.blade.mvc.http.Request;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.service.SiteService;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;
import jetbrick.template.runtime.InterpretContext;

import java.time.LocalDate;
import java.util.Objects;

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
        Request request = WebContext.request();
        String pageType = request.attribute("page_type");
        Integer pageNum = request.attribute("page_num");

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
            title = bingWallPaper.getTitle() + " | " + bingWallPaper.getAttribute();
        } else {
            title = siteService.getTitle(hashCode);
        }

        return title + " | " + BingWallpaperConst.HEAD_TITLE;
    }

    /**
     * 获取页面关键字
     */
    public static String metaKeywords(String hashCode) {
        Request request = WebContext.request();
        String pageType = request.attribute("page_type");

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
        Request request = WebContext.request();
        String pageType = request.attribute("page_type");

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
        Request request = WebContext.request();
        String pageType = request.attribute("page_type");

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
     * 获取图片的访问 URL
     */
    public static String imgHref(BingWallpaper bingWallPaper, String resolution) {
        String imgName = bingWallPaper.getName() + "_" + bingWallPaper.getCode();

        return "/wallpapers/" + imgName + "/" + imgName + "_" + resolution + ".jpg";
    }

    /**
     * 获取壁纸详情页面的 URL
     */
    public static String detailsHref(BingWallpaper bingWallPaper) {
        return "/details/" + bingWallPaper.getName() + "/" + bingWallPaper.getCode();
    }

    /**
     * 分页
     */
    public static Page<BingWallpaper> wallPapers() {
        Request request = WebContext.request();

        Integer pageNum = request.attribute("page_num");
        Integer page = null == pageNum ? 1 : pageNum;
        Integer pageLimit = request.attribute("page_limit");
        Integer limit = null == pageLimit ? 1 : pageLimit;
        String pageType1 = request.attribute("page_type");
        String type = null == pageType1 ? "index" : pageType1;

        Page<BingWallpaper> wallPapers = settingSortingType(Anima.select().from(BingWallpaper.class), type).page(page, limit);
        request.attribute("wallPapers", wallPapers);

        return wallPapers;
    }

    private static AnimaQuery<BingWallpaper> settingSortingType(AnimaQuery<BingWallpaper> query, String type) {
        if (BingWallpaperConst.TOP_CODE.equals(type)) {
            query.order(BingWallpaper::getLikes, OrderBy.DESC);
        } else if (BingWallpaperConst.DOWN_CODE.equals(type)) {
            query.order(BingWallpaper::getDownloads, OrderBy.DESC);
        } else {
            query.order(BingWallpaper::getBid, OrderBy.DESC);
        }

        return query;
    }

    /**
     * 获取图片全称
     */
    public static String getFileName(BingWallpaper bingWallPaper) {
        return bingWallPaper.getName() + "_" + bingWallPaper.getCode() + ".jpg";
    }

    /**
     * 获取当前上下文的必应壁纸对象
     */
    public static BingWallpaper currentBingWallPaper() {
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("wallPaper");

        if (null != value) {
            return (BingWallpaper) value;
        }

        return null;
    }
}
