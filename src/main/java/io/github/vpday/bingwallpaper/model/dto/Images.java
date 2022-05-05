package io.github.vpday.bingwallpaper.model.dto;

import com.hellokaton.blade.kit.StringKit;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author vpday
 * @date 2018/11/23
 */
@Data
public class Images {
    @SerializedName("startdate")
    private String startDate;
    @SerializedName("fullstartdate")
    private String fullStartDate;
    @SerializedName("enddate")
    private String endDate;
    private String url;
    @SerializedName("urlbase")
    private String urlBase;
    private String copyright;
    @SerializedName("copyrightlink")
    private String copyrightLink;
    private String title;
    private String caption;
    @SerializedName("copyrightonly")
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
    private List<?> hs = new LinkedList<>();

    private static final String COPYRIGHT_ICO = "©";

    public String getTitle() {
        if (StringKit.isNotBlank(title)) {
            return title;
        } else {
            return copyright.substring(0, (copyright.lastIndexOf(COPYRIGHT_ICO) - 1)).trim();
        }
    }

    public String getCopyrightOnly() {
        if (StringKit.isNotBlank(copyrightOnly)) {
            return copyrightOnly;
        } else {
            return copyright.substring((copyright.lastIndexOf(COPYRIGHT_ICO)), copyright.length() - 1).trim();
        }
    }

    public String getNameAndCode() {
        return this.urlBase.substring((this.urlBase.lastIndexOf('.') + 1));
    }

    public String getName() {
        String nameAndCode = getNameAndCode();
        return nameAndCode.substring(0, nameAndCode.lastIndexOf('_'));
    }

    public String getCode() {
        String nameAndCode = getNameAndCode();
        return nameAndCode.substring((nameAndCode.lastIndexOf('_') + 1));
    }
}
