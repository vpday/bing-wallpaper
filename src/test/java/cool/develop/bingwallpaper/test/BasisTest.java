package cool.develop.bingwallpaper.test;

import com.blade.kit.JsonKit;
import cool.develop.bingwallpaper.model.dto.CoverStory;
import cool.develop.bingwallpaper.model.dto.ImageArchive;
import io.github.biezhi.request.Request;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Slf4j
public class BasisTest extends BaseTest {

    @Test
    public void requestByCoverStoryTest() {
        String url = "https://cn.bing.com/cnhp/coverstory";

        Request request = Request.get(url)
                .acceptJson()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.62 Safari/537.36");
        if (!request.ok()) {
            log.error("请求失败，状态码：{}，错误信息：{}", request.code(), request.message());
        }
        String json = request.body();

        CoverStory var = JsonKit.formJson(json, CoverStory.class);
        System.out.println(var.toString());
    }

    @Test
    public void requestByImageArchiveTest() {
        String url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

        Request request = Request.get(url)
                .acceptJson()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.62 Safari/537.36");
        if (!request.ok()) {
            log.error("请求失败，状态码：{}，错误信息：{}", request.code(), request.message());
        }
        String json = request.body();

        ImageArchive imageArchive = JsonKit.formJson(json, ImageArchive.class);
        System.out.println(imageArchive.toString());
    }

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
