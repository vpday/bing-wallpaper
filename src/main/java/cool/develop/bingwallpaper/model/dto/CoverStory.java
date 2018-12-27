package cool.develop.bingwallpaper.model.dto;

import com.blade.kit.json.JsonProperty;
import lombok.Data;

/**
 * 封面故事
 *
 * @author vpday
 * @create 2018/11/26
 */
@Data
public class CoverStory {

    /**
     * 展示日期
     */
    private String date;

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
    private String para1;
    private String para2;

    /**
     * 提供者
     */
    private String provider;

    /**
     * 图片链接
     */
    private String imageUrl;

    /**
     * 主要的图片链接
     */
    private String primaryImageUrl;

    /**
     * 国家
     */
    @JsonProperty("Country")
    private String country;

    /**
     * 城市
     */
    @JsonProperty("City")
    private String city;

    /**
     * 经度
     */
    @JsonProperty("Longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @JsonProperty("Latitude")
    private Double latitude;

    /**
     * 所属洲（大陆）
     */
    @JsonProperty("Continent")
    private String continent;

    /**
     * 城市的英语名称
     */
    @JsonProperty("CityInEnglish")
    private String cityInEnglish;

    /**
     * 国家代码
     */
    @JsonProperty("CountryCode")
    private String countryCode;

    /*
    {
      "date": "November 26",
      "title": "童话的幻境",
      "attribute": "美国，纽约",
      "para1": "这座爱丽丝梦游仙境雕塑位于纽约中央公园东侧，靠近75街",
      "para2": "",
      "provider": "© Diego Grandi / Shutterstock",
      "imageUrl": "http://hpimges.blob.core.chinacloudapi.cn/coverstory/watermark_alicecentralpark_zh-cn9031006021_1920x1080.jpg",
      "primaryImageUrl": "http://hpimges.blob.core.chinacloudapi.cn/coverstory/watermark_alicecentralpark_zh-cn9031006021_1920x1080.jpg",
      "Country": "美国",
      "City": "纽约",
      "Longitude": "-73.966536",
      "Latitude": "40.775119",
      "Continent": "北美洲",
      "CityInEnglish": "New York City",
      "CountryCode": "US"
    }
    */
}
