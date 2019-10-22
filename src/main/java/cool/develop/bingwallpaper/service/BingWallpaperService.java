package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import cool.develop.bingwallpaper.bootstrap.properties.ApplicationProperties;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.dto.Images;
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
import java.util.Optional;

/**
 * @author vpday
 * @date 2018/11/23
 */
@Bean
public class BingWallpaperService {

    @Inject
    private ApplicationProperties applicationProperties;

    /**
     * 判断是否已存在壁纸信息
     */
    public boolean isNotExistWallpaper(String name, String hash) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getName, name).and(BingWallpaper::getHash, hash);

        return 0 == animaQuery.count();
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

    public void updateBingWallpaperByHits(String hash, Integer hits) {
        Anima.update().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash)
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
     * 保存图片到本地文件夹
     */
    public void save(Map<String, byte[]> images, String name) throws IOException {
        String filePath = FileUtils.getFilePath((applicationProperties.getBingWallpaperDir() + "/" + name));

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
        String filePath = applicationProperties.getBingWallpaperDir() + "/" + name + "/" + name + "_" + resolution.format() + ".jpg";
        return new File(filePath);
    }
}
