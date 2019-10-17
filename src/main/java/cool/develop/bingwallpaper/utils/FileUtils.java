package cool.develop.bingwallpaper.utils;

import cool.develop.bingwallpaper.exception.TipException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件工具类
 *
 * @author vpday
 * @create 2019/1/25
 */
@Slf4j
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * 创建并获取文件夹
     */
    public static String getFilePath(String name) {
        File directory = new File(name);
        String canonicalPath;

        try {
            if (!directory.exists() && directory.mkdirs()) {
                canonicalPath = directory.getCanonicalPath();
                log.info("用于存放壁纸的文件夹创建成功 [{}]", canonicalPath);
            } else {
                canonicalPath = directory.getCanonicalPath();
            }
        } catch (IOException e) {
            log.error(SiteUtils.getStackTrace(e));
            throw new TipException(e);
        }

        return canonicalPath;
    }

    /**
     * 判断文件夹是否存在
     */
    public static boolean isNotExistFile(String pathName) {
        return !(Files.exists(Paths.get(pathName)));
    }
}
