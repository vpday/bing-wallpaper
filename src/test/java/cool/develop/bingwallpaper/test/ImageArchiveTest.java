package cool.develop.bingwallpaper.test;

import com.blade.kit.NamedThreadFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.biezhi.request.Request;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author vpday
 * @date 2019/1/15
 */
@Slf4j
public class ImageArchiveTest {
    private static final Gson GSON = new Gson();
    private static final String[] LANGUAGE_CODES = new String[]{"es-AR", "es-AE", "ar-EG", "en-IE", "de-AT",
            "en-AU", "pt-BR", "fr-BE", "nl-BE", "pl-PL",
            "da-DK", "de-DE", "ru-RU", "fr-FR", "en-PH",
            "fi-FI", "ko-KR", "nl-NL", "fr-CA", "en-CA",
            "en-MY", "es-US", "en-US", "es-MX", "en-ZA",
            "nb-NO", "pt-PT", "ja-JP", "sv-SE", "de-CH",
            "fr-CH", "ar-SA", "zh-TW", "tr-TR", "es-ES",
            "zh-HK", "en-SG", "en-NZ", "it-IT", "en-IN",
            "en-ID", "en-GB", "es-CL", "zh-CN"};
    private static final String IMAGE_ARCHIVE = "https://global.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&pid=hp&video=1&setlang=en-us&setmkt=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.62 Safari/537.36";

    @Test
    public void testDifferentCountries() {
        List<Future<String>> futures = this.useMultiThreadRequest(this.buildRequestUrl());
        try {
            this.showResult(futures);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private List<Future<String>> useMultiThreadRequest(List<String> urlAll) {
        int initialCapacity = urlAll.size();
        List<Future<String>> futures = new ArrayList<>(initialCapacity);

        ExecutorService executorService = new ThreadPoolExecutor(initialCapacity, initialCapacity + 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new NamedThreadFactory("downLoad@"),
                new ThreadPoolExecutor.AbortPolicy());

        urlAll.forEach(var -> {
            Future<String> future = executorService.submit(() -> this.requestBing(var));
            futures.add(future);
        });

        return futures;
    }

    private String requestBing(String url) {
        Request request = Request.get(url).acceptJson()
                .userAgent(USER_AGENT);

        if (!request.ok()) {
            String message = String.format("请求失败，状态码：%s，URL：%s", request.code(), request.url().toString());
            log.error(message);
            return "";
        }

        return request.body();
    }

    private void showResult(List<Future<String>> futures) throws ExecutionException, InterruptedException {
        for (int i = 0; i < futures.size(); i++) {
            String json = futures.get(i).get();
            if (!(null == json || json.isEmpty())) {
                JsonObject imgInfo = GSON.fromJson(json, JsonObject.class).get("images").getAsJsonArray()
                        .get(0).getAsJsonObject();
                String title = imgInfo.get("title").getAsString();

                if (!(null == title || "".equals(title.trim()))) {
                    String date = imgInfo.get("date").getAsString();
                    System.out.println(String.format("CountryCode: %s ,Date: %s", LANGUAGE_CODES[i], date));
                }
            }
        }
    }

    private List<String> buildRequestUrl() {
        List<String> requestUrl = new ArrayList<>();

        for (String code : LANGUAGE_CODES) {
            String var = IMAGE_ARCHIVE + code;
            requestUrl.add(var);
        }

        return requestUrl;
    }
}
