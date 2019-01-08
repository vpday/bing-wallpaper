package cool.develop.bingwallpaper.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Slf4j
public class BasisTest{

    @Test
    public void testParseUrlBase() {
        String urlBase = "/az/hprichbg/rb/DarwinOrigin_ZH-CN13549933105";

        assertNotNull("urlBase not found", urlBase);
        String nameAndCode = urlBase.substring((urlBase.lastIndexOf("/") + 1));

        assertNotNull("nameAndCode not found", nameAndCode);
        log.info("nameAndCode: {}", nameAndCode);

        String name = nameAndCode.substring(0, nameAndCode.lastIndexOf("_"));
        String code = nameAndCode.substring((nameAndCode.lastIndexOf("_") + 1));
        assertNotNull("name not found", name);
        assertNotNull("code not found", code);
        log.info("name: {}, code: {}", name, code);
    }
}
