package cool.develop.bingwallpaper.model.dto;

import com.blade.kit.json.JsonIgnore;
import com.blade.kit.json.JsonProperty;
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

    @JsonProperty("tooltips")
    @JsonIgnore
    private ToolTips toolTips;
}
