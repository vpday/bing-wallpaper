package cool.develop.bingwallpaper.model.dto;

import lombok.Data;

/**
 * @author vpday
 * @date 2018/11/23
 */
@Data
public class ToolTips {
    private String loading;
    private String previous;
    private String next;
    private String walle;
    private String walls;

    /*
        "tooltips": {
            "loading": "Loading...",
            "previous": "Previous image",
            "next": "Next image",
            "walle": "This image is not available to download as wallpaper.",
            "walls": "Download this image. Use of this image is restricted to wallpaper only."
        }
    */
}
