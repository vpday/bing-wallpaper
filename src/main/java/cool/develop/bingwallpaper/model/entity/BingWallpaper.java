package cool.develop.bingwallpaper.model.entity;

import com.blade.kit.StringKit;
import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Ignore;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 必应壁纸
 *
 * @author vpday
 * @date 2018/11/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_bing_wallpaper", pk = "bid")
public class BingWallpaper extends Model {

    /**
     * 表主键
     */
    private Integer bid;

    /**
     * 图片 hash 码
     */
    private String hash;

    /**
     * 展示日期
     */
    private Long date;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片编号
     */
    private String code;

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
     * 版权
     */
    private String copyright;

    /**
     * 国家编码
     */
    private String country;

    /**
     * 点击次数
     */
    private Integer hits;

    /**
     * 喜欢次数
     */
    private Integer likes;

    /**
     * 下载次数
     */
    private Integer downloads;

    @Ignore
    private FilmingLocation filmingLocation;

    public BingWallpaper() {
    }

    public BingWallpaper(String hash, Long date, String copyright, String country, Integer hits, Integer likes, Integer downloads) {
        this.hash = hash;
        this.date = date;
        this.copyright = copyright;
        this.country = country;
        this.hits = hits;
        this.likes = likes;
        this.downloads = downloads;
    }

    public void parseUrlBase(String urlBase) {
        String nameAndCode = urlBase.substring((urlBase.lastIndexOf('.') + 1));

        this.name = nameAndCode.substring(0, nameAndCode.lastIndexOf('_'));
        this.code = nameAndCode.substring((nameAndCode.lastIndexOf('_') + 1));
    }

    /**
     * 获取页面描述
     */
    public String metaDescription() {
        return StringKit.isBlank(this.getDescription()) ? this.getCopyright() : this.getDescription();
    }

    public String htmlTitle() {
        return StringKit.isBlank(this.getTitle()) ? this.getCopyright() : this.getTitle();
    }
}
