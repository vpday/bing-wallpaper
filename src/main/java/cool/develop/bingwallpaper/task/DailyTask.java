package cool.develop.bingwallpaper.task;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.task.annotation.Schedule;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.service.ServiceHandle;
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
    private ServiceHandle serviceHandle;

    /**
     * 获取当日的封面故事和图片存档
     */
    @Schedule(name = "get-daily-archive-zero", cron = "30 0 0 * * ?")
    public void getCoverStoryAndImageArchiveByZero() {
        log.info("开始执行 get-daily-archive-zero 任务");
        serviceHandle.saveBingWallpaper(CountryCode.ZH_CN, CountryCode.JA_JP, CountryCode.EN_AU);
    }

    @Schedule(name = "get-daily-archive-three", cron = "30 0 3 * * ?")
    public void getCoverStoryAndImageArchiveByThree() {
        log.info("开始执行 get-daily-archive-three 任务");
        serviceHandle.saveBingWallpaper(CountryCode.EN_IN);
    }

    @Schedule(name = "get-daily-archive-seven", cron = "30 0 7 * * ?")
    public void getCoverStoryAndImageArchiveBySeven() {
        log.info("开始执行 get-daily-archive-seven 任务");
        serviceHandle.saveBingWallpaper(CountryCode.DE_DE, CountryCode.FR_FR);
    }

    @Schedule(name = "get-daily-archive-eight", cron = "30 0 8 * * ?")
    public void getCoverStoryAndImageArchiveByEight() {
        log.info("开始执行 get-daily-archive-eight 任务");
        serviceHandle.saveBingWallpaper(CountryCode.EN_GB);
    }

    @Schedule(name = "get-daily-archive-twelve", cron = "30 0 12 * * ?")
    public void getCoverStoryAndImageArchiveByTwelve() {
        log.info("开始执行 get-daily-archive-twelve 任务");
        serviceHandle.saveBingWallpaper(CountryCode.EN_CA);
    }

    @Schedule(name = "get-daily-archive-thirteen", cron = "30 0 15 * * ?")
    public void getCoverStoryAndImageArchiveByThirteen() {
        log.info("开始执行 get-daily-archive-thirteen 任务");
        serviceHandle.saveBingWallpaper(CountryCode.EN_CA);
    }

    /**
     * 每周一下午十五点十分执行，补漏
     */
    @Schedule(name = "get-daily-archive-thirteen-days", cron = "30 30 10 ? * WED")
    public void getCoverStoryAndImageArchiveByFifteenDays() {
        log.info("开始执行 get-daily-archive-thirteen-days 任务");
        serviceHandle.saveBingWallpaperByFifteenDays();
    }
}
