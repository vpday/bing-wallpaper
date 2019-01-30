package cool.develop.bingwallpaper.model.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 图片分辨率 格式化
 *
 * @author vpday
 * @create 2018/11/23
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

    public static boolean checkParam(String width, String height) {
        long count = RESOLUTIONS.stream()
                .filter(var -> var.getWidth().equals(Integer.valueOf(width)) && var.getHeight().equals(Integer.valueOf(height)))
                .count();

        return 0 < count;
    }

    public String format() {
        return this.getWidth() + "x" + this.getHeight();
    }
}
