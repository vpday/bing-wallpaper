package cool.develop.bingwallpaper.utils;

import com.blade.kit.DateKit;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.extension.Site;
import cool.develop.bingwallpaper.model.dto.CoverStory;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.service.BingService;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import io.github.biezhi.request.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 工具类
 *
 * @author vpday
 * @create 2018/11/23
 */
@Slf4j
public final class SiteUtils {

    public static String buildImageArchiveUrl(Integer index, Integer number) {
        return BingWallpaperConst.IMAGE_ARCHIVE + "?" +
                "format=js&" +
                "idx=" + index + "&" +
                "n=" + number + "&" +
                "mkt=zh-CN";
    }

    public static String requestBing(String url) {
        Request request = Request.get(url)
                .acceptJson()
                .userAgent(BingWallpaperConst.USER_AGENT);
        checkStatus(request);

        return request.body();
    }

    public static byte[] downLoadWallpaper(String url) {
        Request request = Request.get(url).userAgent(BingWallpaperConst.USER_AGENT);
        checkStatus(request);

        return request.bytes();
    }

    private static void checkStatus(Request request) {
        if (!request.ok()) {
            String message = String.format("请求失败，状态码：%s，URL：%s", request.code(), request.url().toString());
            log.error(message);
            throw new TipException(message);
        }
    }

    public static String getFilePath(String hash) {
        File directory = new File(hash);
        String canonicalPath;

        try {
            if (!directory.exists() && directory.mkdirs()) {
                canonicalPath = directory.getCanonicalPath();
                log.info("用于存放壁纸的文件夹创建成功 [{}]", canonicalPath);
            } else {
                canonicalPath = directory.getCanonicalPath();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new TipException(e.getMessage());
        }

        return canonicalPath;
    }

    public static void saveCoverStoryAndImageArchive(BingService bingService, BingWallpaperService bingWallpaperService) {
        CoverStory coverStory = bingService.getCoverStory();
        Images images = bingService.getImageArchiveByToDay();
        Map<String, byte[]> downLoadImages;
        try {
            downLoadImages = bingService.downLoadImages(images);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            throw new TipException(e.getMessage());
        }

        bingWallpaperService.save(coverStory, images);
        try {
            bingWallpaperService.save(downLoadImages, images.getNameAndCode());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new TipException(e.getMessage());
        }
    }

    public static String getRssXml(List<BingWallpaper> wallpapers) throws FeedException {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle(BingWallpaperConst.HEAD_TITLE);
        channel.setLink(BingWallpaperConst.SITE_URL);
        channel.setDescription(BingWallpaperConst.META_DESCRIPTION);
        channel.setLanguage("zh-CN");
        channel.setCopyright(BingWallpaperConst.META_AUTHOR);

        List<Item> items = new ArrayList<>();
        wallpapers.forEach(wallpaper -> {
            Item item = new Item();
            item.setTitle(wallpaper.getCopyright());

            Content content = new Content();
            String url = BingWallpaperConst.SITE_URL + Site.imgHref(wallpaper, "1920x1080");
            content.setValue("<img src=\"" + url + "\" border=\"0\"/><h2>"
                    + wallpaper.getTitle() + " —— " + wallpaper.getAttribute() + " | " + wallpaper.detailedLocation() + "</h2><h4>"
                    + wallpaper.getDescription() + "</h4>");
            item.setContent(content);

            item.setLink(BingWallpaperConst.SITE_URL + Site.detailsHref(wallpaper));
            item.setPubDate(DateKit.toDate(wallpaper.getShowDate(), BingWallpaperConst.DATE_PATTERN_DB));
            items.add(item);
        });
        channel.setItems(items);

        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }
}
