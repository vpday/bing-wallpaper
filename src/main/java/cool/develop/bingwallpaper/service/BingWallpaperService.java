package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import cool.develop.bingwallpaper.bootstrap.BingWallpaperConst;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.dto.Images;
import cool.develop.bingwallpaper.model.dto.LifeInfo;
import cool.develop.bingwallpaper.model.dto.Resolution;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.model.entity.FilmingLocation;
import cool.develop.bingwallpaper.utils.FileUtils;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.core.JoinParam;
import io.github.biezhi.anima.core.Joins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Bean
public class BingWallpaperService {

    /**
     * 判断是否已存在壁纸信息
     */
    public boolean isNotExistWallpaper(String hash) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash);

        return 0 == animaQuery.count();
    }

    public Optional<BingWallpaper> getBingWallpaper(String name, String code) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getName, name)
                .and(BingWallpaper::getCode, code);

        return Optional.ofNullable(animaQuery.one());
    }

    public Optional<BingWallpaper> getBingWallpaper(String name, CountryCode countryCode) {
        JoinParam param = Joins.with(FilmingLocation.class).as(BingWallpaper::getFilmingLocation)
                .on(BingWallpaper::getName, FilmingLocation::getName);
        param.setFieldName("filmingLocation");

        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .join(param)
                .where(BingWallpaper::getName, name)
                .and(BingWallpaper::getCountry, countryCode.code());

        return Optional.ofNullable(animaQuery.one());
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

    public void updateBingWallpaperByHits(String name, CountryCode countryCode, Integer hits) {
        Anima.update().from(BingWallpaper.class)
                .where(BingWallpaper::getName, name)
                .and(BingWallpaper::getCountry, countryCode.code())
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
    public synchronized void save(Long date, Images images, CountryCode country) {
        BingWallpaper bingWallPaper = new BingWallpaper(
                images.getHsh(),
                date,
                images.getCopyright(),
                country.code(),
                1, 1, 1
        );

        bingWallPaper.setTitle(images.getTitle());
        bingWallPaper.setCaption(images.getCaption());
        bingWallPaper.setDescription(images.getDesc());

        bingWallPaper.parseUrlBase(images.getUrlBase());
        bingWallPaper.save();
    }

    /**
     * 保存拍摄地点信息
     */
    @Deprecated
    public synchronized void save(String name, LifeInfo lifeInfo) {
        FilmingLocation filmingLocation = new FilmingLocation(name, lifeInfo.getAttribute());

        if (Objects.nonNull(lifeInfo.getLatitude()) && Objects.nonNull(lifeInfo.getLongitude())) {
            String mapUrl = BingWallpaperConst.GOOGLE_MAP_URL + lifeInfo.getLatitude() + "," + lifeInfo.getLongitude();
            filmingLocation.setLatitude(lifeInfo.getLatitude());
            filmingLocation.setLongitude(lifeInfo.getLongitude());
            filmingLocation.setMapUrl(mapUrl);
        }

        filmingLocation.save();
    }

    /**
     * 保存图片到本地文件夹
     */
    public void save(Map<String, byte[]> images, String name) throws IOException {
        String filePath = FileUtils.getFilePath((BingWallpaperConst.BING_WALLPAPER_DIR + "/" + name));

        for (Map.Entry<String, byte[]> var : images.entrySet()) {
            String pathName = filePath + "/" + var.getKey();

            if (FileUtils.isNotExistFile(pathName)) {
                Files.write(Paths.get(pathName), var.getValue());
            }
        }
    }

    /**
     * 加载壁纸文件
     */
    public File load(String name, Resolution resolution) {
        String filePath = BingWallpaperConst.BING_WALLPAPER_DIR + "/" + name + "/" + name + "_" + resolution.format() + ".jpg";
        return new File(filePath);
    }
}
