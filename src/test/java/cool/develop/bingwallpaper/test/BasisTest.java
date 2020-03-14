package cool.develop.bingwallpaper.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

/**
 * @author vpday
 * @date 2018/11/23
 */
@Slf4j
public class BasisTest {

    @Test
    public void testParseUrlBase() {
        String urlBase = "/th?id=OHR.SpainRioTinto_EN-US0146116496";
        assertNotNull("urlBase not found", urlBase);

        String nameAndCode = urlBase.substring((urlBase.lastIndexOf(".") + 1));
        assertNotNull("nameAndCode not found", nameAndCode);

        log.info("nameAndCode: {}", nameAndCode);

        String name = nameAndCode.substring(0, nameAndCode.lastIndexOf("_"));
        assertNotNull("name not found", name);

        String code = nameAndCode.substring((nameAndCode.lastIndexOf("_") + 1));
        assertNotNull("code not found", code);

        log.info("name: {}, code: {}", name, code);
    }

    @Test
    public void testParseDate() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().parseLenient()
                .appendPattern("[MMM. dd, yyyy]")
                .appendPattern("[MMM dd, yyyy]")
                .appendPattern("[MM dd, yyyy]")
                .appendPattern("[yyyyMMdd]");
        assertNotNull(builder);

        System.out.println(LocalDate.parse("févr. 20, 2019", builder.toFormatter(Locale.FRANCE)));
        System.out.println(LocalDate.parse("Feb 20, 2019", builder.toFormatter(Locale.ENGLISH)));
        System.out.println(LocalDate.parse("2 20, 2019", builder.toFormatter(Locale.JAPAN)));
        System.out.println(LocalDate.parse("20190304", builder.toFormatter(Locale.GERMANY)));
    }

    @Test
    public void testCopyright() {
        String copyrightText = "泰河畔伊尔的Les Orgues，法国北加泰罗尼亚 (© SFL Choice/Alamy)";
        assertNotNull("copyrightText not blank", copyrightText);

        String title = copyrightText.substring(0, (copyrightText.lastIndexOf("©") - 1)).trim();
        assertNotNull("title not blank", title);

        log.info("title: {}", title);

        String copyrightOnly = copyrightText.substring((copyrightText.lastIndexOf("©")), copyrightText.length() - 1).trim();
        assertNotNull("copyrightOnly not blank", copyrightOnly);

        log.info("copyrightOnly: {}", copyrightOnly);
    }
}
