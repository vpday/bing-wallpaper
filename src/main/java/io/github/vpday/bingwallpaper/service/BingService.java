package io.github.vpday.bingwallpaper.service;

import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.kit.JsonKit;
import io.github.vpday.bingwallpaper.bootstrap.BingWallpaperConst;
import io.github.vpday.bingwallpaper.model.dto.ImageArchive;
import io.github.vpday.bingwallpaper.model.dto.Images;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.model.enums.ResolutionEnum;
import io.github.vpday.bingwallpaper.utils.SiteUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Bing Service
 *
 * @author vpday
 * @date 2018/11/23
 */
@Bean
public class BingService {

    /**
     * 获取当日图片存档
     */
    public Images getImageArchiveByToDay() {
        return this.getImageArchiveByToDay(CountryCodeEnum.ZH_CN);
    }

    /**
     * 获取当日图片存档
     */
    public Images getImageArchiveByToDay(CountryCodeEnum country) {
        String json = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(0, 1, country.code()));
        ImageArchive imageArchive = JsonKit.fromJson(json, ImageArchive.class);

        return imageArchive.getImages().get(0);
    }

    /**
     * 获取最近 15 天的图片存档
     */
    public List<Images> getImageArchiveByFifteenDays(CountryCodeEnum country) {
        String sevenDays = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(0, 7, country.code()));
        List<Images> images = JsonKit.fromJson(sevenDays, ImageArchive.class).getImages();

        String eightDays = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(16, 8, country.code()));
        images.addAll(JsonKit.fromJson(eightDays, ImageArchive.class).getImages());

        return images;
    }

    /**
     * 下载已知分辨率的所有图片
     */
    public Map<String, byte[]> downLoadImages(Images images) throws ExecutionException, InterruptedException {
        List<ResolutionEnum> resolutionEnumList = Arrays.asList(ResolutionEnum.values());
        List<String> urlAll = new ArrayList<>(resolutionEnumList.size());
        List<String> imageNames = new ArrayList<>(resolutionEnumList.size());
        String name = images.getName();

        resolutionEnumList.forEach(var -> {
            String suffix = "_" + var.width() + "x" + var.height() + BingWallpaperConst.IMAGE_FILE_SUFFIX;
            String url = BingWallpaperConst.BING + images.getUrlBase() + suffix;

            urlAll.add(url);
            imageNames.add(name + suffix);
        });

        int initialCapacity = (int) Math.pow(2, (resolutionEnumList.size() * 1.0) / 2 + 1);
        Map<String, byte[]> file = new ConcurrentHashMap<>(initialCapacity);
        List<Future<byte[]>> futures = this.useMultiThread(urlAll);
        for (int i = 0, length = resolutionEnumList.size(); i < length; i++) {
            file.put(imageNames.get(i), futures.get(i).get());
        }

        return file;
    }

    private List<Future<byte[]>> useMultiThread(List<String> urlAll) {
        int initialCapacity = urlAll.size();
        List<Future<byte[]>> futures = new ArrayList<>(initialCapacity);

        ExecutorService executorService = SiteUtils.newFixedThreadPool(initialCapacity, "downLoadImg@");

        urlAll.forEach(var -> {
            Future<byte[]> future = executorService.submit(() -> SiteUtils.downLoadWallpaper(var));
            futures.add(future);
        });

        executorService.shutdown();
        return futures;
    }
}
