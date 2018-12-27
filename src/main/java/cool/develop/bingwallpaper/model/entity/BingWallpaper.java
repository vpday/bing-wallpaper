package cool.develop.bingwallpaper.model.entity;

import com.blade.kit.StringKit;
import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 必应壁纸
 *
 * @author vpday
 * @create 2018/11/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_bing_wall_paper", pk = "bid")
public class BingWallpaper extends Model {

    /**
     * 必应壁纸表主键
     */
    private Integer bid;

    /**
     * 图片 hash 码
     */
    private String hash;

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
     * 归属地
     */
    private String attribute;

    /**
     * 描述
     */
    private String description;

    /**
     * 版权
     */
    private String copyright;

    /**
     * 版权链接
     */
    private String copyrightLink;

    /**
     * 展示日期
     */
    private String showDate;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * Google Map 链接
     */
    private String mapUrl;

    /**
     * 所属洲（大陆）
     */
    private String continent;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

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

    public BingWallpaper() {
    }

    public BingWallpaper(String hash, String title, String attribute, String description, String copyright, String copyrightLink, String showDate, String continent) {
        this.hash = hash;
        this.title = title;
        this.attribute = attribute;
        this.description = description;
        this.copyright = copyright;
        this.copyrightLink = copyrightLink;
        this.showDate = showDate;
        this.continent = continent;
    }

    public void parseUrlBase(String urlBase) {
        String nameAndCode = urlBase.substring((urlBase.lastIndexOf("/") + 1));

        this.name = nameAndCode.substring(0, nameAndCode.lastIndexOf("_"));
        this.code = nameAndCode.substring((nameAndCode.lastIndexOf("_") + 1));
    }

    public String getFullName() {
        return this.getName() + "_" + this.getCode();
    }

    public String getKeywords() {
        return this.getCopyright() + ", " + this.getTitle() + ", " + this.getAttribute() + ", " + this.detailedLocation() + ", " + this.getShowDate();
    }

    /**
     * 获取组合地址
     */
    public String detailedLocation() {
        StringBuilder location = new StringBuilder();
        location.append(this.getContinent());

        if (StringKit.isNotEmpty(this.getCountry())) {
            location.append(", ").append(this.getCountry());
        }
        if (StringKit.isNotEmpty(this.getCity())) {
            location.append(", ").append(this.getCity());
        }

        return location.toString();
    }
}
