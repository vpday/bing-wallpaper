package io.github.vpday.bingwallpaper.test.service;

import com.hellokaton.blade.ioc.annotation.Inject;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.model.dto.Images;
import io.github.vpday.bingwallpaper.service.BingService;
import io.github.vpday.bingwallpaper.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author vpday
 * @date 2019/1/7
 */
@Slf4j
public class BingServiceTest extends BaseTest {

    @Inject
    private BingService bingService;

    @Test
    public void testGetImageArchiveByToDay() {
        Images images = bingService.getImageArchiveByToDay();
        assertNotNull("images isn't null", images);
        log.info(images.toString());
    }

    @Test
    public void testGetImageArchiveByFifteenDays() {
        List<Images> images = bingService.getImageArchiveByFifteenDays(CountryCodeEnum.ZH_CN);
        assertNotNull("image list isn't null", images);
        images.forEach(var -> log.info(var.toString()));
    }
}
