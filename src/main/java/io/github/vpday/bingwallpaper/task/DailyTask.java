package io.github.vpday.bingwallpaper.task;

import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.task.annotation.Schedule;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.service.ServiceHandle;
import lombok.extern.slf4j.Slf4j;

/**
 * 每日任务
 *
 * @author vpday
 * @date 2018/11/23
 */
@Bean
@Slf4j
public class DailyTask {

    @Inject
    private ServiceHandle serviceHandle;

    /**
     * 获取当日的封面故事和图片存档
     */
    @Schedule(name = "get-daily-archive-zero", cron = "30 1 0 * * ?")
    public void getCoverStoryAndImageArchiveByZero() {
        log.info("开始执行 get-daily-archive-zero 任务 ZH_CN、JA_JP、EN_AU");
        serviceHandle.saveBingWallpaper(CountryCodeEnum.ZH_CN, CountryCodeEnum.JA_JP, CountryCodeEnum.EN_AU);
    }

    @Schedule(name = "get-daily-archive-three", cron = "30 0 3 * * ?")
    public void getCoverStoryAndImageArchiveByThree() {
        log.info("开始执行 get-daily-archive-three 任务 EN_IN");
        serviceHandle.saveBingWallpaper(CountryCodeEnum.EN_IN);
    }

    @Schedule(name = "get-daily-archive-seven", cron = "30 0 7 * * ?")
    public void getCoverStoryAndImageArchiveBySeven() {
        log.info("开始执行 get-daily-archive-seven 任务 DE_DE、FR_FR");
        serviceHandle.saveBingWallpaper(CountryCodeEnum.DE_DE, CountryCodeEnum.FR_FR);
    }

    @Schedule(name = "get-daily-archive-eight", cron = "30 0 8 * * ?")
    public void getCoverStoryAndImageArchiveByEight() {
        log.info("开始执行 get-daily-archive-eight 任务 EN_GB");
        serviceHandle.saveBingWallpaper(CountryCodeEnum.EN_GB);
    }

    @Schedule(name = "get-daily-archive-twelve", cron = "30 0 12 * * ?")
    public void getCoverStoryAndImageArchiveByTwelve() {
        log.info("开始执行 get-daily-archive-twelve 任务 EN_CA");
        serviceHandle.saveBingWallpaper(CountryCodeEnum.EN_CA);
    }

    @Schedule(name = "get-daily-archive-thirteen", cron = "30 0 15 * * ?")
    public void getCoverStoryAndImageArchiveByThirteen() {
        log.info("开始执行 get-daily-archive-thirteen 任务 EN_US");
        serviceHandle.saveBingWallpaper(CountryCodeEnum.EN_US);
    }

    /**
     * 每周一下午十五点十分执行，补漏
     */
    @Schedule(name = "get-daily-archive-thirteen-days", cron = "0 40 15 ? * SAT")
    public void getCoverStoryAndImageArchiveByFifteenDays() {
        log.info("开始执行 get-daily-archive-thirteen-days 任务");
        serviceHandle.saveBingWallpaperByFifteenDays();
    }
}
