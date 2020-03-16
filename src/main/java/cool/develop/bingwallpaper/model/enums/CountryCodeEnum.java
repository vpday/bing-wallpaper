package cool.develop.bingwallpaper.model.enums;

import io.github.biezhi.anima.annotation.EnumMapping;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 国家编码
 *
 * @author vpday
 * @date 2019/1/23
 */
@Getter
@EnumMapping("code")
public enum CountryCodeEnum {

    /**
     * 中国
     */
    ZH_CN("zh-CN","zh", "中国"),

    /**
     * 日本
     */
    JA_JP("ja-JP", "ja","日本"),

    /**
     * 印度
     */
    EN_IN("en-IN", "en","印度"),

    /**
     * 澳大利亚
     */
    EN_AU("en-AU", "en","澳大利亚"),

    /**
     * 加拿大
     */
    EN_CA("en-CA", "en","加拿大"),

    /**
     * 美国
     */
    EN_US("en-US", "eb","美国"),

    /**
     * 英国
     */
    EN_GB("en-GB", "en","英国"),

    /**
     * 法国
     */
    FR_FR("fr-FR", "fr","法国"),

    /**
     * 德国
     */
    DE_DE("de-DE", "de","德国");

    /**
     * 编码
     */
    private String code;

    /**
     * 语言
     */
    private String language;

    /**
     * 中文名称
     */
    private String cnName;

    public String code() {
        return code;
    }

    public String language() {
        return language;
    }

    public String cnName() {
        return cnName;
    }

    CountryCodeEnum() {
    }

    CountryCodeEnum(String code, String language, String cnName) {
        this.code = code;
        this.language = language;
        this.cnName = cnName;
    }

    public static CountryCodeEnum getCountryCode(String code) {
        Optional<CountryCodeEnum> optionalLang = Arrays.stream(CountryCodeEnum.values())
                .filter(var -> var.code.equalsIgnoreCase(code)).findFirst();
        return optionalLang.orElse(CountryCodeEnum.ZH_CN);
    }

    public static boolean isExistCode(String code) {
        long count = Arrays.stream(CountryCodeEnum.values())
                .filter(var -> var.code.equalsIgnoreCase(code)).count();
        return 0 < count;
    }

    public static boolean isNotExistCode(String code) {
        return !isExistCode(code);
    }
}
