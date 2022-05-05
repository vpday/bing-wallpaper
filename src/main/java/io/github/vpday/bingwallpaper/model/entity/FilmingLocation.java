package io.github.vpday.bingwallpaper.model.entity;

import com.hellokaton.anima.Model;
import com.hellokaton.anima.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拍摄地点表
 *
 * @author vpday
 * @date 2019/1/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_filming_location", pk = "fid")
public class FilmingLocation extends Model {

    /**
     * 表主键
     */
    private Integer fid;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 归属地
     */
    private String attribute;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * Google Map 链接
     */
    private String mapUrl;

    public FilmingLocation() {
    }

    public FilmingLocation(String name, String attribute) {
        this.name = name;
        this.attribute = attribute;
    }
}
