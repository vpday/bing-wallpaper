package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.JsonKit;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.dto.*;
import cool.develop.bingwallpaper.utils.SiteUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
 * @create 2018/11/23
 */
@Bean
public class BingService {

    /**
     * 可获取最近几天的封面故事
     */
    public LifeInfo getLifeInfo(LocalDate date) throws IOException {
        String newUrl = BingWallpaperConst.LIFE + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Document document = Jsoup.connect(newUrl).userAgent(BingWallpaperConst.USER_AGENT).get();

        return LifeInfo.parseDocument(document);
    }

    /**
     * 获取当日图片存档
     */
    public Images getImageArchiveByToDay() {
        return this.getImageArchiveByToDay(CountryCode.ZH_CN);
    }

    /**
     * 获取当日图片存档
     */
    public Images getImageArchiveByToDay(CountryCode country) {
        String json = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(0, 1, country.code()));
        ImageArchive imageArchive = JsonKit.formJson(json, ImageArchive.class);

        return imageArchive.getImages().get(0);
    }

    /**
     * 获取最近 15 天的图片存档
     */
    public List<Images> getImageArchiveByFifteenDays() {
        return this.getImageArchiveByFifteenDays(CountryCode.ZH_CN);
    }

    /**
     * 获取最近 15 天的图片存档
     */
    public List<Images> getImageArchiveByFifteenDays(CountryCode country) {
        String sevenDays = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(0, 7, country.code()));
        List<Images> images = ((ImageArchive) JsonKit.formJson(sevenDays, ImageArchive.class)).getImages();

        String eightDays = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(16, 8, country.code()));
        images.addAll(((ImageArchive) JsonKit.formJson(eightDays, ImageArchive.class)).getImages());

        return images;
    }

    /**
     * 下载已知分辨率的所有图片
     */
    public Map<String, byte[]> downLoadImages(Images images) throws ExecutionException, InterruptedException {
        List<String> urlAll = new ArrayList<>(Resolution.RESOLUTIONS.size());
        List<String> imageNames = new ArrayList<>(Resolution.RESOLUTIONS.size());
        String name = images.getName();

        Resolution.RESOLUTIONS.forEach(var -> {
            String suffix = "_" + var.getWidth() + "x" + var.getHeight() + BingWallpaperConst.IMAGE_FILE_SUFFIX;
            String url = BingWallpaperConst.BING + images.getUrlBase() + suffix;

            urlAll.add(url);
            imageNames.add(name + suffix);
        });

        int initialCapacity = (int) Math.pow(2, (Resolution.RESOLUTIONS.size() * 1.0) / 2 + 1);
        Map<String, byte[]> file = new ConcurrentHashMap<>(initialCapacity);
        List<Future<byte[]>> futures = this.useMultiThread(urlAll);
        for (int i = 0; i < Resolution.RESOLUTIONS.size(); i++) {
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
