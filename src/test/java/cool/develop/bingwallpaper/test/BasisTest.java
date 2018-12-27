package cool.develop.bingwallpaper.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Slf4j
public class BasisTest extends BaseTest {

    @Test
    public void parseUrlBaseTest() {
        String urlBase = "/az/hprichbg/rb/DarwinOrigin_ZH-CN13549933105";

        String nameAndCode = urlBase.substring((urlBase.lastIndexOf("/") + 1));
        log.info("nameAndCode: {}", nameAndCode);

        String name = nameAndCode.substring(0, nameAndCode.lastIndexOf("_"));
        String code = nameAndCode.substring((nameAndCode.lastIndexOf("_") + 1));
        log.info("name: {}, code: {}", name, code);
    }
}
