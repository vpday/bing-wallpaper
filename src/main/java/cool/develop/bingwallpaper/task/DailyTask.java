package cool.develop.bingwallpaper.task;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.task.annotation.Schedule;
import cool.develop.bingwallpaper.exception.TipException;
import cool.develop.bingwallpaper.model.dto.CoverStory;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.service.BingService;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 每日任务
 *
 * @author vpday
 * @create 2018/11/23
 */
@Bean
@Slf4j
public class DailyTask {

    @Inject
    private BingService bingService;

    @Inject
    private BingWallpaperService bingWallpaperService;

    /**
     * 获取当日的封面故事和图片存档
     */
    @Schedule(name = "get-daily-archive", cron = "0 0 0 * * ?")
    public void getCoverStoryAndImageArchive() {
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
