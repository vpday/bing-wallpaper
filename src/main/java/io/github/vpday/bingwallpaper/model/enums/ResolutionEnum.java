package io.github.vpday.bingwallpaper.model.enums;

/**
 * @author vpday
 * @date 2020/3/16 21:14
 */
public enum ResolutionEnum {

    /**
     * hd 1080p
     */
    HD_1080P(1920, 1080),

    /**
     * mobile wxga 360p
     */
    MOBILE_WXGA_360P(640, 360);

    private Integer width;

    private Integer height;

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

    ResolutionEnum() {
    }

    ResolutionEnum(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public String format() {
        return width + "x" + height;
    }
}
