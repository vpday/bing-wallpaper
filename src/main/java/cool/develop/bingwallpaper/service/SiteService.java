package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.sun.syndication.io.FeedException;
import cool.develop.bingwallpaper.bootstrap.properties.ApplicationProperties;
import cool.develop.bingwallpaper.extension.Site;
import cool.develop.bingwallpaper.model.dto.CountryCode;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.model.vo.Sitemap;
import cool.develop.bingwallpaper.utils.SiteUtils;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Site Service
 *
 * @author vpdy
 * @date 2018/11/23
 */
@Bean
public class SiteService {

    @Inject
    private ApplicationProperties applicationProperties;

    public String getRssXml(CountryCode countryCode) throws FeedException {
        List<BingWallpaper> bingWallpapers = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCountry, countryCode.code())
                .order(BingWallpaper::getBid, OrderBy.DESC).all();

        return SiteUtils.getRssXml(bingWallpapers, countryCode, applicationProperties);
    }

    public List<BingWallpaper> getAllByCountry(CountryCode country) {
        AnimaQuery<BingWallpaper> query = Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCountry, country.code())
                .order(BingWallpaper::getDate, OrderBy.DESC);

        return query.all();
    }

    public List<Sitemap> getSitemapUrls(CountryCode country) {
        List<BingWallpaper> wallpapers = this.getAllByCountry(country);

        List<Sitemap> sitemapList = new ArrayList<>(wallpapers.size());
        wallpapers.forEach(item -> {
            String siteUrl = applicationProperties.getSiteUrl();
            String loc = siteUrl + Site.detailsHref(item);
            String lastmod = Site.unixTimeToString(item.getDate());

            sitemapList.add(new Sitemap(loc, lastmod, item.getCountry(), siteUrl));
        });

        return sitemapList;
    }

    public List<Sitemap> getSitemapUrls() {
        List<CountryCode> lists = Arrays.asList(CountryCode.values());
        List<Sitemap> sitemapList = new ArrayList<>(lists.size());

        lists.forEach(item -> {
            String siteUrl = applicationProperties.getSiteUrl();
            String loc = siteUrl + "/sitemap/" + item.code() + ".xml";
            String lastmod = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

            sitemapList.add(new Sitemap(loc, lastmod, item.code(), siteUrl));
        });

        return sitemapList;
    }
}
