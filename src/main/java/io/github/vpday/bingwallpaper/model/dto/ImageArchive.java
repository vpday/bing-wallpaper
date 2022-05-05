package io.github.vpday.bingwallpaper.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片存档
 *
 * @author vpday
 * @date 2018/11/23
 */
@Data
public class ImageArchive {
    private List<Images> images = new ArrayList<>(16);
}
