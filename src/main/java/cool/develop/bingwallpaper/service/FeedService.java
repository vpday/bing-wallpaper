package cool.develop.bingwallpaper.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import cool.develop.bingwallpaper.bootstrap.properties.ApplicationProperties;
import cool.develop.bingwallpaper.extension.Site;
import cool.develop.bingwallpaper.model.entity.BingWallpaper;
import cool.develop.bingwallpaper.model.enums.CountryCodeEnum;
import cool.develop.bingwallpaper.model.vo.Sitemap;

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
public class FeedService {

    @Inject
    private ApplicationProperties applicationProperties;

    public List<Sitemap> listAllSitemapUrls(CountryCodeEnum country, List<BingWallpaper> bingWallpapers) {
        List<Sitemap> sitemapList = new ArrayList<>(bingWallpapers.size());

        final String siteUrl = applicationProperties.getSiteUrl();
        bingWallpapers.forEach(item -> {
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

    public List<Sitemap> listAllSitemapUrls() {
        List<CountryCodeEnum> lists = Arrays.asList(CountryCodeEnum.values());
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
}
