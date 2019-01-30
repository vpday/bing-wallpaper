package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.sun.syndication.io.FeedException;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.model.entity.FilmingLocation;
import cool.develop.bingwallpaper.utils.SiteUtils;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.enums.OrderBy;

import java.util.List;

/**
 * Site Service
 *
 * @author vpdy
 * @create 2018/11/23
 */
@Bean
public class SiteService {

    public String getTitle(String hash) {
        return this.getBingWallpaperByHash(hash).getTitle();
    }

    public String getCopyright(String hash) {
        return this.getBingWallpaperByHash(hash).getCopyright();
    }

    public String getKeywords(String hash) {
        return this.getBingWallpaperByHash(hash).getKeywords();
    }

    public String getDescription(String hash) {
        return this.getBingWallpaperByHash(hash).getDescription();
    }

    public String getAttribute(String name) {
        FilmingLocation filmingLocation = this.getFilmingLocationByName(name);
        return null == filmingLocation ? "" : filmingLocation.getAttribute();
    }

    public String getMapUrl(String name) {
        FilmingLocation filmingLocation = this.getFilmingLocationByName(name);
        return null == filmingLocation ? "" : filmingLocation.getMapUrl();
    }

    public String getRssXml(CountryCode countryCode) throws FeedException {
        List<BingWallpaper> bingWallpapers = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCountry, countryCode.code())
                .order(BingWallpaper::getBid, OrderBy.DESC).all();

        return SiteUtils.getRssXml(bingWallpapers, countryCode);
    }

    private BingWallpaper getBingWallpaperByHash(String hash) {
        return Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash).one();
    }

    private FilmingLocation getFilmingLocationByName(String name) {
        return Anima.select().from(FilmingLocation.class)
                .where(FilmingLocation::getName, name).one();
    }
}
