package cool.develop.bingwallpaper.model.dto;

import com.blade.kit.json.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpday
 * @date 2018/11/23
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
        return this.urlBase.substring((this.urlBase.lastIndexOf(".") + 1));
    }

    public String getName() {
        String nameAndCode = getNameAndCode();
        return nameAndCode.substring(0, nameAndCode.lastIndexOf("_"));
    }

    public String getCode() {
        String nameAndCode = getNameAndCode();
        return nameAndCode.substring((nameAndCode.lastIndexOf("_") + 1));
    }

    /*
        {
          "images": [
            {
              "startdate": "20190312",
              "fullstartdate": "201903120700",
              "enddate": "20190313",
              "url": "/th?id=OHR.SpainRioTinto_EN-US0146116496_1920x1080.jpg&rf=NorthMale_1920x1080.jpg&pid=hp",
              "urlbase": "/th?id=OHR.SpainRioTinto_EN-US0146116496",
              "copyright": "Channels of the Rio Tinto in Spain (© Oscar Diez Martinez/Minden Pictures)",
              "copyrightlink": "/search?q=rio+tinto+river+spain&form=hpcapt&filters=HpDate%3a%2220190312_0700%22",
              "title": "Treasures of the Rio Tinto",
              "caption": "The otherworldly red river",
              "copyrightonly": "© Oscar Diez Martinez/Minden Pictures",
              "desc": "The Rio Tinto, in Andalusia, Spain, gets its strange color from dissolving iron deposits in the highly acidic water. Beginning more than 5,000 years ago, this area was mined for gold, silver, and other treasures. And archeological evidence suggests that about 3,000 years ago this may have been the site of King Solomon’s legendary mines. Scientists believe that pollution from the mines contributed to the extreme ecological conditions we see in the Rio Tinto today.",
              "date": "Mar 12, 2019",
              "bsTitle": "Treasures of the Rio Tinto",
              "quiz": "/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20190312_SpainRioTinto%22&FORM=HPQUIZ",
              "wp": true,
              "hsh": "1f0c7a6d4bcf9d391463a7e30db9f4b9",
              "drk": 1,
              "top": 1,
              "bot": 1,
              "hs": [

              ]
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
