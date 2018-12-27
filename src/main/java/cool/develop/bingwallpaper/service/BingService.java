package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.JsonKit;
import com.blade.kit.NamedThreadFactory;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.dto.CoverStory;
import cool.develop.bingwallpaper.model.dto.ImageArchive;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.model.dto.Resolution;
import cool.develop.bingwallpaper.utils.SiteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Bing Service
 *
 * @author vpday
 * @create 2018/11/23
 */
@Bean
public class BingService {
    /**
     * 获取当日 Bing 故事
     */
    public CoverStory getCoverStory() {
        String json = SiteUtils.requestBing(BingWallpaperConst.COVER_STORY);
        return JsonKit.formJson(json, CoverStory.class);
    }

    /**
     * 获取当日图片存档
     */
    public Images getImageArchiveByToDay() {
        String json = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(0, 1));
        ImageArchive imageArchive = JsonKit.formJson(json, ImageArchive.class);

        return imageArchive.getImages().get(0);
    }

    /**
     * 获取最近 15 天的图片存档
     */
    public List<Images> getImageArchiveByFifteenDays() {
        String sevenDays = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(0, 7));
        List<Images> images = ((ImageArchive) JsonKit.formJson(sevenDays, ImageArchive.class)).getImages();

        String eightDays = SiteUtils.requestBing(SiteUtils.buildImageArchiveUrl(16, 8));
        images.addAll(((ImageArchive) JsonKit.formJson(eightDays, ImageArchive.class)).getImages());

        return images;
    }

    /**
     * 下载已知分辨率的所有图片
     */
    public Map<String, byte[]> downLoadImages(Images images) throws ExecutionException, InterruptedException {
        List<String> urlAll = new ArrayList<>(Resolution.RESOLUTIONS.size());
        List<String> imageNames = new ArrayList<>(Resolution.RESOLUTIONS.size());

        String nameAndCode = images.getNameAndCode();

        Resolution.RESOLUTIONS.forEach(var -> {
            String suffix = "_" + var.getWidth() + "x" + var.getHeight() + BingWallpaperConst.IMAGE_FILE_SUFFIX;
            String url = BingWallpaperConst.CN_BING + images.getUrlBase() + suffix;

            urlAll.add(url);
            imageNames.add(nameAndCode + suffix);
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

        ExecutorService executorService = new ThreadPoolExecutor(initialCapacity, initialCapacity + 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1024),
                new NamedThreadFactory("downLoad"),
                new ThreadPoolExecutor.AbortPolicy());

        urlAll.forEach(var -> {
            Future<byte[]> future = executorService.submit(() -> SiteUtils.downLoadWallpaper(var));
            futures.add(future);
        });

        return futures;
    }
}
