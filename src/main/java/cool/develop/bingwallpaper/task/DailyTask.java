package cool.develop.bingwallpaper.task;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.task.annotation.Schedule;
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
    @Schedule(name = "get-daily-archive", cron = "0 0 4 * * ?")
    public void getCoverStoryAndImageArchive() {
        serviceHandle.saveBingWallpaper();
    }
}
