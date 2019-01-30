package cool.develop.bingwallpaper.test.service;

import com.blade.ioc.annotation.Inject;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.service.BingService;
import cool.develop.bingwallpaper.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author vpday
 * @create 2019/1/7
 */
@Slf4j
public class BingServiceTest extends BaseTest {

    @Inject
    private BingService bingService;

    @Test
    public void testGetImageArchiveByToDay() {
        log.info(bingService.getImageArchiveByToDay().toString());
    }

    @Test
    public void testGetImageArchiveByFifteenDays() {
        bingService.getImageArchiveByFifteenDays(CountryCode.ZH_CN).forEach(var -> log.info(var.toString()));
    }
}
