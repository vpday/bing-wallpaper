package cool.develop.bingwallpaper.utils;

import com.blade.kit.NamedThreadFactory;
import com.blade.kit.StringKit;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.exception.TipException;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工具类
 *
 * @author vpday
 * @date 2018/11/23
 */
@Slf4j
public final class SiteUtils {

    private SiteUtils() {
    }

    public static String buildImageArchiveUrl(Integer index, Integer number, String mkt) {
        return BingWallpaperConst.IMAGE_ARCHIVE + "?" +
                "format=js&pid=hp&video=1&setlang=en-us&" +
                "idx=" + index + "&" +
                "n=" + number + "&" +
                "mkt=" + mkt;
    }

    public static String requestBing(String url) {
        io.github.biezhi.request.Request request = io.github.biezhi.request.Request.get(url).acceptJson()
                .userAgent(BingWallpaperConst.USER_AGENT);

        checkStatus(request);
        return request.body();
    }

    public static byte[] downLoadWallpaper(String url) {
        io.github.biezhi.request.Request request = io.github.biezhi.request.Request.get(url).userAgent(BingWallpaperConst.USER_AGENT);
        checkStatus(request);

        return request.bytes();
    }

    private static void checkStatus(io.github.biezhi.request.Request request) {
        if (!request.ok()) {
            String message = String.format("请求失败，状态码：%s，URL：%s", request.code(), request.url().toString());
            log.error(message);
            throw new TipException(message);
        }
    }

    /**
     * 构建线程池
     */
    public static ExecutorService newFixedThreadPool(int corePoolSize, String threadPrefix) {
        return new ThreadPoolExecutor(corePoolSize, (corePoolSize / 2 + corePoolSize),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new NamedThreadFactory(threadPrefix),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static Locale acceptLanguage(com.blade.mvc.http.Request request) {
        Locale locale = Locale.CHINA;

        String languages = request.header("Accept-Language");
        if (StringKit.isBlank(languages)) {
            return locale;
        }

        String languages2 = languages.replace(" ", "").toLowerCase();
        if (languages2.startsWith("accept-language:")) {
            languages2 = languages2.substring(16);
        }

        List<String> localeList = Arrays.asList(languages2.split(","));
        if (!localeList.isEmpty()) {
            Optional<Locale> useLocal = Locale.LanguageRange.parse(localeList.get(0)).stream()
                    .sorted(Comparator.comparing(Locale.LanguageRange::getWeight).reversed())
                    .map(range -> new Locale(range.getRange())).findFirst();
            return useLocal.orElse(Locale.CHINA);
        }

        return locale;
    }
}
