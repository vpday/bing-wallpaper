package cool.develop.bingwallpaper.model.dto;

import com.blade.kit.json.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpday
 * @create 2018/11/23
 */
@Data
public class Images {
    @JsonProperty("startdate")
    private String startDate;
    @JsonProperty("fullstartdate")
    private String fullStartDate;
    @JsonProperty("enddate")
    private String endDate;
    private String url;
    @JsonProperty("urlbase")
    private String urlBase;
    private String copyright;
    @JsonProperty("copyrightlink")
    private String copyrightLink;
    private String title;
    private String quiz;
    private Boolean wp;
    private String hsh;
    private Integer drk;
    private Integer top;
    private Integer bot;
    private List hs = new ArrayList();

    public String getNameAndCode() {
        return this.urlBase.substring((this.urlBase.lastIndexOf("/") + 1));
    }

    /*
        {
            "startdate": "20181123",
            "fullstartdate": "201811231600",
            "enddate": "20181124",
            "url": "/az/hprichbg/rb/DarwinOrigin_ZH-CN13549933105_1920x1080.jpg",
            "urlbase": "/az/hprichbg/rb/DarwinOrigin_ZH-CN13549933105",
            copyright": "站在巨龟头上的达尔文雀 (© Tui De Roy/Minden Pictures)",
            "copyrightlink": "http://www.bing.com/search?q=%E8%BE%BE%E5%B0%94%E6%96%87%E9%9B%80&form=hpcapt&mkt=zh-cn",
            "title": "",
            "quiz": "/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20181123_DarwinOrigin%22&FORM=HPQUIZ",
            "wp": true,
            "hsh": "919c6133d2da462d3d5ef750151a0659",
            "drk": 1,
            "top": 1,
            "bot": 1,
            "hs": []
        }
    */
}
