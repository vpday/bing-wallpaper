package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.utils.FileUtils;
import cool.develop.bingwallpaper.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * @author vpday
 * @create 2019/1/25
 */
@Bean
@Slf4j
public class ServiceHandle {

    @Inject
    private BingService bingService;

    @Inject
    private BingWallpaperService bingWallpaperService;


    /**
     * 获取并存储当日必应壁纸
     */
    public void saveBingWallpaper() {
        CountryCode[] countryCode = CountryCode.values();
        ExecutorService executorService = SiteUtils.newFixedThreadPool(countryCode.length, "country@");

        for (CountryCode var : countryCode) {
            executorService.submit(() -> {
                // 获取图片存档信息
                Images images = bingService.getImageArchiveByToDay(var);
                long epochMilli = SiteUtils.parseDate(var, images);
                try {
                    this.saveImage(var, images, epochMilli);
                } catch (ExecutionException | InterruptedException | IOException e) {
                    log.error(e.getMessage());
                    throw new TipException(e.getMessage());
                }
            });
        }

        executorService.shutdown();
    }

    /**
     * 获取并存储最近 15 天的必应壁纸
     */
    public void saveBingWallpaperByFifteenDays() {
        CountryCode[] countryCode = CountryCode.values();
        ExecutorService executorService = SiteUtils.newFixedThreadPool(countryCode.length, "country@");

        for (CountryCode country : countryCode) {
            // 获取图片存档信息
            List<Images> images = bingService.getImageArchiveByFifteenDays(country);
            Collections.reverse(images);

            this.useMultiThread(executorService, country, images);
        }

        executorService.shutdown();
    }

    /**
     * 为每个国家都开启一个线程处理
     */
    private void useMultiThread(ExecutorService executorService, CountryCode country, List<Images> images) {
        executorService.submit(() -> {
            int length = images.size();
            ExecutorService executorService2 = SiteUtils.newFixedThreadPool(length, "images@");

            for (Images image : images) {
                this.useMultiThread2(executorService2, country, image);
            }

            executorService2.shutdown();
        });
    }

    /**
     * 每张图片都开启一个保存图片的线程
     */
    private void useMultiThread2(ExecutorService executorService2, CountryCode country, Images images) {
        executorService2.submit(() -> {
            try {
                this.saveImage(country, images, SiteUtils.parseDate(country, images));
            } catch (ExecutionException | InterruptedException | IOException e) {
                log.error(e.getMessage());
                throw new TipException(e.getMessage());
            }
        });
    }

    private void saveImage(CountryCode country, Images images, Long date)
            throws ExecutionException, InterruptedException, IOException {
        // 存储数据
        bingWallpaperService.save(date, images, country);

        String pathName = BingWallpaperConst.BING_WALLPAPER_DIR + "/" + images.getName();
        if (FileUtils.isNotExistFile(pathName)) {
            // 获取壁纸文件
            Map<String, byte[]> downLoadImages = bingService.downLoadImages(images);
            // 存储壁纸
            bingWallpaperService.save(downLoadImages, images.getName());
        }
    }
}
