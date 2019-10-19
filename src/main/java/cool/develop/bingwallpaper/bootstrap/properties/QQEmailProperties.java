package cool.develop.bingwallpaper.bootstrap.properties;

import com.blade.Environment;
import lombok.Data;

/**
 * 邮件配置属性
 *
 * @author vpday
 * @date 2019/10/19
 */
@Data
public class QQEmailProperties {

    /**
     * 是否启用
     */
    private boolean enable = false;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 收件人
     */
    private String addressee;

    public QQEmailProperties init(Environment environment) {
        QQEmailProperties qqEmailProperties = new QQEmailProperties();

        boolean enable1 = environment.getBoolean("app.qq_email.enable", Boolean.FALSE);
        qqEmailProperties.setEnable(enable1);

        String username1 = environment.get("app.qq_email.username", "");
        qqEmailProperties.setUsername(username1);

        String password1 = environment.get("app.qq_email.password", "");
        qqEmailProperties.setPassword(password1);

        String addressee1 = environment.get("app.email.addressee", "");
        qqEmailProperties.setAddressee(addressee1);

        return qqEmailProperties;
    }
}
