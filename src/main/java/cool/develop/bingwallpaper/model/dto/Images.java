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
    private String caption;
    @JsonProperty("copyrightonly")
    private String copyrightOnly;
    private String desc;
    private String date;
    private String bsTitle;
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

    public String getName() {
        String nameAndCode = getNameAndCode();
        return nameAndCode.substring(0, nameAndCode.lastIndexOf("_"));
    }

    /*
        {
          "images": [
            {
              "startdate": "20190123",
              "fullstartdate": "201901230800",
              "enddate": "20190124",
              "url": "/az/hprichbg/rb/ApfelTag_EN-US4068796758_1920x1080.jpg",
              "urlbase": "/az/hprichbg/rb/ApfelTag_EN-US4068796758",
              "copyright": "On Pie Day, an apple tree in winter (© Chris Stein/Getty Images)",
              "copyrightlink": "/search?q=Apple+tree&form=hpcapt&filters=HpDate%3a%2220190123_0800%22",
              "title": "Pie in the sky",
              "caption": "Celebrating Pie Day is as easy as, well…",
              "copyrightonly": "© Chris Stein/Getty Images",
              "desc": "Can these frosty apples be salvaged in time for National Pie Day today?",
              "date": "Jan 23, 2019",
              "bsTitle": "Pie in the sky",
              "quiz": "/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20190123_ApfelTag%22&FORM=HPQUIZ",
              "wp": true,
              "hsh": "a28e1ab7431edc318defc9b9fdb26425",
              "drk": 1,
              "top": 1,
              "bot": 1,
              "hs": []
            }
          ],
          "tooltips": {
            "loading": "Loading...",
            "previous": "Previous image",
            "next": "Next image",
            "walle": "This image is not available to download as wallpaper.",
            "walls": "Download this image. Use of this image is restricted to wallpaper only.",
            "play": "Play video",
            "pause": "Pause video"
          }
        }
    */
}
