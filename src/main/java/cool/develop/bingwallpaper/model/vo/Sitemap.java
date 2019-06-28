package cool.develop.bingwallpaper.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vpday
 * @create 2019/6/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sitemap {

    private String loc;
    private String lastmod;
    private String hreflang;
    private String defaultHref;

}
