package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.model.dto.LifeInfo;
import cool.develop.bingwallpaper.utils.FileUtils;
import cool.develop.bingwallpaper.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
    public void saveBingWallpaper() throws IOException {
        LocalDate showDate = LocalDate.now();
        long epochMilli = showDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli();

        // 获取封面故事信息
        LifeInfo lifeInfo = bingService.getLifeInfo(showDate);
        lifeInfo.setDate(epochMilli);

        CountryCode[] countryCode = CountryCode.values();
        ExecutorService executorService = SiteUtils.newFixedThreadPool(countryCode.length, "country@");
        for (CountryCode var : countryCode) {
            executorService.submit(() -> {
                // 获取图片存档信息
                Images images = bingService.getImageArchiveByToDay(var);
                try {
                    this.saveImage(var, images, lifeInfo);
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

    private void useMultiThread(ExecutorService executorService, CountryCode country, List<Images> images) {
        LocalDate localDate = LocalDate.now();

        executorService.submit(() -> {
            int length = images.size();
            ExecutorService executorService2 = SiteUtils.newFixedThreadPool(length, "images@");

            for (int i = 0; i < length; i++) {
                LocalDate showDate = localDate.minusDays(length - 1 - i);
                this.useMultiThread2(executorService2, showDate, country, images.get(i));
            }

            executorService2.shutdown();
        });
    }

    private void useMultiThread2(ExecutorService executorService2, LocalDate showDate, CountryCode country, Images images) {
        long epochMilli = showDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli();

        executorService2.submit(() -> {
            try {
                // 获取封面故事信息
                LifeInfo lifeInfo = bingService.getLifeInfo(showDate);
                lifeInfo.setDate(epochMilli);
                // 保持图片
                this.saveImage(country, images, lifeInfo);
            } catch (ExecutionException | InterruptedException | IOException e) {
                log.error(e.getMessage());
                throw new TipException(e.getMessage());
            }
        });
    }

    private void saveImage(CountryCode country, Images images, LifeInfo lifeInfo)
            throws ExecutionException, InterruptedException, IOException {
        // 存储数据
        bingWallpaperService.save(lifeInfo, images, country);
        if (country.equals(CountryCode.ZH_CN)) {
            bingWallpaperService.save(images.getName(), lifeInfo);
        }

        String pathName = BingWallpaperConst.BING_WALLPAPER_DIR + "/" + images.getName();
        if (FileUtils.isNotExistFile(pathName)) {
            // 获取壁纸文件
            Map<String, byte[]> downLoadImages = bingService.downLoadImages(images);
            // 存储壁纸
            bingWallpaperService.save(downLoadImages, images.getName());
        }
    }
}
