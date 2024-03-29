package io.github.vpday.bingwallpaper.service;

import com.hellokaton.anima.Anima;
import com.hellokaton.anima.core.AnimaQuery;
import com.hellokaton.anima.core.JoinParam;
import com.hellokaton.anima.core.Joins;
import com.hellokaton.anima.enums.OrderBy;
import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.ioc.annotation.Inject;
import io.github.vpday.bingwallpaper.bootstrap.properties.ApplicationProperties;
import io.github.vpday.bingwallpaper.model.dto.Images;
import io.github.vpday.bingwallpaper.model.entity.BingWallpaper;
import io.github.vpday.bingwallpaper.model.entity.FilmingLocation;
import io.github.vpday.bingwallpaper.model.enums.CountryCodeEnum;
import io.github.vpday.bingwallpaper.model.enums.ResolutionEnum;
import io.github.vpday.bingwallpaper.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
    public boolean isNotExistWallpaper(String code) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCode, code);
        return 0 == animaQuery.count();
    }

    public Optional<BingWallpaper> getBingWallpaper(String name, CountryCodeEnum countryCodeEnum) {
        JoinParam param = Joins.with(FilmingLocation.class).as(BingWallpaper::getFilmingLocation)
                .on(BingWallpaper::getName, FilmingLocation::getName);
        param.setFieldName("filmingLocation");

        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .join(param)
                .where(BingWallpaper::getName, name)
                .and(BingWallpaper::getCountry, countryCodeEnum.code());

        return Optional.ofNullable(animaQuery.one());
    }

    public BingWallpaper getBingWallpaper(String hash) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash);

        return animaQuery.one();
    }

    public List<BingWallpaper> listAllBy(CountryCodeEnum country, int limit) {
        return Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCountry, country.code())
                .order(BingWallpaper::getBid, OrderBy.DESC)
                .limit(limit);
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
    public synchronized void save(Long date, Images images, CountryCodeEnum countryCodeEnum) {
        BingWallpaper bingWallPaper = new BingWallpaper(
                images.getHsh(),
                date,
                images.getCopyrightOnly(),
                countryCodeEnum,
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
    public File load(String name, ResolutionEnum resolutionEnum) {
        String filePath = applicationProperties.getBingWallpaperDir() + "/" + name + "/" + name + "_" + resolutionEnum.format() + ".jpg";
        return new File(filePath);
    }
}
