package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.dto.CoverStory;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.model.dto.Resolution;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.utils.SiteUtils;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Bean
public class BingWallpaperService {

    /**
     * 判断是否存在当天壁纸信息
     */
    public boolean isNotExistToDayWallpaper() {
        String today = DateKit.toString(LocalDate.now(), BingWallpaperConst.DATE_PATTERN_DB);

        AnimaQuery<BingWallpaper> animaQuery = Anima.select()
                .from(BingWallpaper.class)
                .where(BingWallpaper::getShowDate, today);

        return 0 == animaQuery.count();
    }

    public BingWallpaper getBingWallpaper(String name, String code) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getName, name)
                .and(BingWallpaper::getCode, code);

        return animaQuery.one();
    }

    public BingWallpaper getBingWallpaper(String hash) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash);

        return animaQuery.one();
    }

    public void updateBingWallpaperByHits(String name, String code, Integer hits) {
        Anima.update().from(BingWallpaper.class)
                .where(BingWallpaper::getName, name)
                .and(BingWallpaper::getCode, code)
                .set(BingWallpaper::getHits, hits).execute();
    }

    public void updateBingWallpaperByLikes(String hash, Integer likes) {
        Anima.update().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash).set(BingWallpaper::getLikes, likes).execute();
    }

    public void updateBingWallpaperByDownLoads(String hash, Integer downloads) {
        Anima.update().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash).set(BingWallpaper::getDownloads, downloads).execute();
    }

    /**
     * 保存信息到数据库
     */
    public void save(CoverStory coverStory, Images images) {
        String endDate = DateKit.toString(DateKit.toLocalDate(images.getEndDate(),
                BingWallpaperConst.DATE_PATTERN), BingWallpaperConst.DATE_PATTERN_DB);

        BingWallpaper bingWallPaper = new BingWallpaper(
                images.getHsh(),
                coverStory.getTitle(),
                coverStory.getAttribute(),
                coverStory.getPara1(),
                images.getCopyright(),
                images.getCopyrightLink(),
                endDate,
                coverStory.getContinent()
        );

        bingWallPaper.setHits(1);
        bingWallPaper.setLikes(1);
        bingWallPaper.setDownloads(1);

        if (Objects.nonNull(coverStory.getLatitude()) && Objects.nonNull(coverStory.getLongitude())) {
            String mapUrl = BingWallpaperConst.GOOGLE_MAP_URL + coverStory.getLatitude() + "," + coverStory.getLongitude();
            bingWallPaper.setLatitude(coverStory.getLatitude());
            bingWallPaper.setLongitude(coverStory.getLongitude());
            bingWallPaper.setMapUrl(mapUrl);
        }

        if (StringKit.isNotEmpty(coverStory.getCountry())) {
            bingWallPaper.setCountry(coverStory.getCountry());
        }
        if (StringKit.isNotEmpty(coverStory.getCity())) {
            bingWallPaper.setCity(coverStory.getCity());
        }

        bingWallPaper.parseUrlBase(images.getUrlBase());

        bingWallPaper.save();
    }

    /**
     * 保存图片到本地文件夹
     */
    public void save(Map<String, byte[]> images, String nameAndCode) throws IOException {
        String filePath = SiteUtils.getFilePath((BingWallpaperConst.BING_WALLPAPER_DIR + "/" + nameAndCode));

        for (Map.Entry<String, byte[]> var : images.entrySet()) {
            String path = filePath + "/" + var.getKey();
            Files.write(Paths.get(path), var.getValue());
        }
    }

    public File load(String fullName, Resolution resolution) {
        String filePath = BingWallpaperConst.BING_WALLPAPER_DIR + "/" + fullName + "/" + fullName + "_" + resolution.format() + ".jpg";
        return new File(filePath);
    }
}
