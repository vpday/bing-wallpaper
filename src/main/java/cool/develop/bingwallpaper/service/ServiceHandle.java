package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.kit.CollectionKit;
import com.blade.kit.StringKit;
import cool.develop.bingwallpaper.bootstrap.properties.ApplicationProperties;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.model.enums.CountryCodeEnum;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.utils.DateUtils;
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
 * @date 2019/1/25
 */
@Bean
@Slf4j
public class ServiceHandle {

    private static final String THREAD_PREFIX = "country@";

    @Inject
    private BingService bingService;

    @Inject
    private BingWallpaperService bingWallpaperService;

    @Inject
    private ApplicationProperties applicationProperties;

    /**
     * 获取并存储全部国家的当日必应壁纸
     */
    public void saveBingWallpaper() {
        this.saveBingWallpaper(CountryCodeEnum.values());
    }

    /**
     * 获取并存储指定国家的当日必应壁纸
     */
    public void saveBingWallpaper(CountryCodeEnum... countryCodeEnum) {
        if (CollectionKit.isNotEmpty(countryCodeEnum)) {
            ExecutorService executorService = SiteUtils.newFixedThreadPool(countryCodeEnum.length, THREAD_PREFIX);

            for (CountryCodeEnum code : countryCodeEnum) {
                executorService.submit(() -> this.saveBingWallpaper(code));
            }

            executorService.shutdown();
        }
    }

    /**
     * 获取并存储指定国家的当日必应壁纸
     */
    public void saveBingWallpaper(CountryCodeEnum countryCodeEnum) {
        // 获取图片存档信息
        Images images = bingService.getImageArchiveByToDay(countryCodeEnum);
        // 判断是否已存在该壁纸信息
        if (bingWallpaperService.isNotExistWallpaper(images.getCode())) {
            // 存储数据
            this.saveExceptionHandle(countryCodeEnum, images);

            String date = StringKit.isEmpty(images.getDate()) ? images.getEndDate() : images.getDate();
            log.info("保存壁纸 {}，时间 {}", images.getNameAndCode(), date);
        }
    }

    /**
     * 获取并存储最近 15 天的必应壁纸
     */
    public void saveBingWallpaperByFifteenDays() {
        CountryCodeEnum[] countryCodeEnum = CountryCodeEnum.values();
        ExecutorService executorService = SiteUtils.newFixedThreadPool(countryCodeEnum.length, THREAD_PREFIX);

        for (CountryCodeEnum country : countryCodeEnum) {
            // 获取图片存档信息
            List<Images> images = bingService.getImageArchiveByFifteenDays(country);
            Collections.reverse(images);

            this.useMultiThread(executorService, country, images);
        }

        executorService.shutdown();
    }

    /**
     * 多线程下载和存储的处理
     */
    private void useMultiThread(ExecutorService executorService, CountryCodeEnum country, List<Images> images) {
        // 为每个国家都开启一个线程处理
        executorService.submit(() -> {
            int length = images.size();
            ExecutorService executorService2 = SiteUtils.newFixedThreadPool(length, "images@");

            for (Images image : images) {
                // 判断是否已存在该壁纸信息
                if (bingWallpaperService.isNotExistWallpaper(image.getCode())) {
                    // 每张图片都开启一个保存图片的线程
                    executorService2.submit(() -> {
                        // 存储数据
                        this.saveExceptionHandle(country, image);

                        String date = StringKit.isEmpty(image.getDate()) ? image.getEndDate() : image.getDate();
                        log.info("保存壁纸 {}，时间 {}", image.getNameAndCode(), date);
                    });
                }
            }

            executorService2.shutdown();
        });
    }

    /**
     * 初始化数据库
     */
    public void initDataBases() {
        CountryCodeEnum[] countryCodeEnum = CountryCodeEnum.values();
        ExecutorService executorService = SiteUtils.newFixedThreadPool(countryCodeEnum.length, THREAD_PREFIX);

        for (CountryCodeEnum country : countryCodeEnum) {
            // 获取图片存档信息
            List<Images> images = bingService.getImageArchiveByFifteenDays(country);
            Collections.reverse(images);

            // 为每个国家都开启一个线程处理
            executorService.submit(() -> {
                int length = images.size();
                ExecutorService executorService2 = SiteUtils.newFixedThreadPool(length, "images@");

                for (Images image : images) {
                    // 每张图片都开启一个保存图片的线程，存储数据
                    executorService2.submit(() -> this.saveExceptionHandle(country, image));
                }

                executorService2.shutdown();
            });
        }

        executorService.shutdown();
    }

    /**
     * 处理在存储壁纸时可能会发生的异常
     */
    private void saveExceptionHandle(CountryCodeEnum country, Images images) {
        try {
            this.saveImage(country, images, DateUtils.parseDate(country, images));
        } catch (Exception e) {
            log.error(SiteUtils.getStackTrace(e));
            throw new TipException(e);
        }
    }

    /**
     * 存储壁纸文件到本地
     * 存储壁纸数据到数据库
     */
    private void saveImage(CountryCodeEnum country, Images images, Long date) throws ExecutionException, InterruptedException, IOException {
        // 存储数据
        bingWallpaperService.save(date, images, country);

        String pathName = applicationProperties.getBingWallpaperDir() + "/" + images.getName();
        if (FileUtils.isNotExistFile(pathName)) {
            // 获取壁纸文件
            Map<String, byte[]> downLoadImages = bingService.downLoadImages(images);
            // 存储壁纸
            bingWallpaperService.save(downLoadImages, images.getName());
        }
    }
}
