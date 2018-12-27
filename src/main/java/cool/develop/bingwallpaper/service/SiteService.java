package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;

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

    private BingWallpaper getBingWallpaperByHash(String hash) {
        AnimaQuery<BingWallpaper> animaQuery = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getHash, hash);

        return animaQuery.one();
    }
}
