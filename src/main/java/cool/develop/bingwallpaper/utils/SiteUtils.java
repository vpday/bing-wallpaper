package cool.develop.bingwallpaper.utils;

import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.model.dto.CoverStory;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.service.BingService;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import io.github.biezhi.request.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
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
}
