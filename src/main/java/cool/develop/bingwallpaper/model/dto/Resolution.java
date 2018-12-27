package cool.develop.bingwallpaper.model.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 已知图片分辨率 格式化
 * 1920x1080
 * 1366x768
 * 1280x768
 * 1024x768
 * 800x600
 * 800x480
 * 768x1280
 * 720x1280
 * 640x480
 * 640x360
 * 480x800
 * 400x240
 * 320x240
 * 240x320
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
            new Resolution(1366, 768),
            new Resolution(1280, 768),
            new Resolution(1024, 768),
            new Resolution(800, 600),
            new Resolution(800, 480),
            new Resolution(768, 1280),
            new Resolution(720, 1280),
            new Resolution(640, 480),
            new Resolution(640, 360),
            new Resolution(480, 800),
            new Resolution(400, 240),
            new Resolution(320, 240),
            new Resolution(240, 320)
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
