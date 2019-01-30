package cool.develop.bingwallpaper.model.dto;

import cool.develop.bingwallpaper.exception.TipException;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author vpday
 * @create 2019/1/25
 */
@Data
public class LifeInfo {
    /**
     * 展示日期
     */
    private Long date;

    /**
     * 标题
     */
    private String title;

    /**
     * 说明
     */
    private String caption;

    /**
     * 描述
     */
    private String description;

    /**
     * 归属地
     */
    private String attribute;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    public LifeInfo() {
    }

    public LifeInfo(String title, String caption, String description, String attribute) {
        this.title = title;
        this.caption = caption;
        this.description = description;
        this.attribute = attribute;
    }

    public static LifeInfo parseDocument(Document document) {
        if (null == document) {
            throw new TipException("document 对象为空");
        }

        Elements ttl = document.getElementsByClass("hplaTtl");
        Elements attr = document.getElementsByClass("hplaAttr");

        Element snippet = document.getElementById("hplaSnippet");

        Elements tt = document.getElementsByClass("hplatt");
        Elements ts = document.getElementsByClass("hplats");
        String caption = tt.get(0).html() + " —— " + ts.get(0).html();

        LifeInfo lifeInfo = new LifeInfo(ttl.get(0).html(), caption, snippet.html(), attr.get(0).html());

        String href = document.getElementsByClass("hplaDMLink").attr("href");
        if (!(null == href || href.isEmpty())) {
            String lat = href.substring(href.indexOf("lat=") + 4, href.indexOf("&lon="));
            String lon = href.substring(href.indexOf("lon=") + 4, href.indexOf("&ct="));

            lifeInfo.setLatitude(Double.valueOf(lat));
            lifeInfo.setLongitude(Double.valueOf(lon));
        }

        return lifeInfo;
    }
}
