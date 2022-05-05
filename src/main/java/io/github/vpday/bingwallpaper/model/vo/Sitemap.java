package io.github.vpday.bingwallpaper.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vpday
 * @date 2019/6/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sitemap {

    private String loc;
    private String lastmod;
    private String hreflang;
    private String defaultHref;

    private Boolean hasImage = Boolean.FALSE;
    private String imageLoc;
    private String imageCaption;
    private String imageTitle;

    public Sitemap(String loc, String lastmod, String hreflang, String defaultHref) {
        this.loc = loc;
        this.lastmod = lastmod;
        this.hreflang = hreflang;
        this.defaultHref = defaultHref;
    }
}
