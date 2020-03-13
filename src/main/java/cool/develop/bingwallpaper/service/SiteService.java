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
        return SiteUtils.getRssXml(this.listAllByCountry(countryCode), countryCode, applicationProperties);
    }

    public List<Sitemap> getSitemapUrls(CountryCode country) {
        List<BingWallpaper> wallpapers = this.listAllByCountry(country);
        List<Sitemap> sitemapList = new ArrayList<>(wallpapers.size());

        final String siteUrl = applicationProperties.getSiteUrl();
        wallpapers.forEach(item -> {
            String defaultHref = siteUrl + Site.detailsDefaultHref(item);
            String loc = siteUrl + Site.detailsHref(item);
            String lastmod = Site.unixTimeToString(item.getDate());
            String imageLoc = siteUrl + Site.imgHrefByHD(item);
            String imageCaption = item.getCopyright();
            String imageTitle = item.getTitle();
            sitemapList.add(new Sitemap(loc, lastmod, country.language(), defaultHref, Boolean.TRUE, imageLoc, imageCaption, imageTitle));
        });

        return sitemapList;
    }

    public List<Sitemap> getSitemapUrls() {
        List<CountryCode> lists = Arrays.asList(CountryCode.values());
        List<Sitemap> sitemapList = new ArrayList<>(lists.size());

        final String siteUrl = applicationProperties.getSiteUrl();
        final String defaultHref = siteUrl + "/sitemap.xml";
        lists.forEach(item -> {
            String loc = siteUrl + "/sitemap/" + item.code() + ".xml";
            String lastmod = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            sitemapList.add(new Sitemap(loc, lastmod, item.code(), defaultHref));
        });

        return sitemapList;
    }

    private List<BingWallpaper> listAllByCountry(CountryCode country) {
        return Anima.select().from(BingWallpaper.class)
                .where(BingWallpaper::getCountry, country.code())
                .order(BingWallpaper::getBid, OrderBy.DESC)
                .limit(30);
    }
}
