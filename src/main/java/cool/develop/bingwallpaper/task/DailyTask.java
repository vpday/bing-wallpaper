package cool.develop.bingwallpaper.task;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.task.annotation.Schedule;
import cool.develop.bingwallpaper.service.BingService;
import cool.develop.bingwallpaper.service.BingWallpaperService;
import cool.develop.bingwallpaper.utils.SiteUtils;
import lombok.extern.slf4j.Slf4j;

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
        SiteUtils.saveCoverStoryAndImageArchive(bingService, bingWallpaperService);
    }
}
