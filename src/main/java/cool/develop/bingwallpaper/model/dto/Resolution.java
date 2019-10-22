package cool.develop.bingwallpaper.model.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 图片分辨率 格式化
 *
 * @author vpday
 * @date 2018/11/23
 */
@Data
public class Resolution {

    private Integer width;

    private Integer height;

    public static final List<Resolution> RESOLUTIONS = Arrays.asList(
            new Resolution(1920, 1080),
            new Resolution(800, 480),
            new Resolution(640, 360),
            new Resolution(400, 240)
    );

    public Resolution() {
    }

    public Resolution(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public String format() {
        return this.getWidth() + "x" + this.getHeight();
    }
}
