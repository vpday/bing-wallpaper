package cool.develop.bingwallpaper.test;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author vpday
 * @create 2019/1/15
 */
@Slf4j
@Deprecated
public class LifeTest {

    private static final String URL = "https://www.bing.com/cnhp/life?mkt=zh-CN&currentDate=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.62 Safari/537.36";

    @Test
    @Ignore
    public void testParseHtml() {
        String newUrl = URL + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Document document = null;

        try {
            document = Jsoup.connect(newUrl).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null == document) {
            throw new RuntimeException("document 对象为空");
        }

        Elements ttl = document.getElementsByClass("hplaTtl");
        Elements attr = document.getElementsByClass("hplaAttr");

        Elements tt = document.getElementsByClass("hplatt");
        Elements ts = document.getElementsByClass("hplats");
        Element snippet = document.getElementById("hplaSnippet");

        System.out.println(ttl.get(0).html() + " —— " + attr.get(0).html());
        System.out.println(tt.get(0).html() + " —— " + ts.get(0).html());
        System.out.println(snippet.html());

        Elements location = document.getElementsByClass("hplaDMLink");
        String href = location.attr("href");
        if (!(null == href || href.isEmpty())) {
            System.out.println(href);
            String lat = href.substring(href.indexOf("lat=") + 4, href.indexOf("&lon="));
            String lon = href.substring(href.indexOf("lon=") + 4, href.indexOf("&ct="));

            System.out.println("纬度: " + lat);
            System.out.println("经度: " + lon);
        }
    }
}
