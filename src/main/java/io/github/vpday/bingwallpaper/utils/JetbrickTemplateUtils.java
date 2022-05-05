package io.github.vpday.bingwallpaper.utils;

import jetbrick.template.JetTemplate;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author vpday
 * @date 2020/3/14 17:08
 */
public final class JetbrickTemplateUtils {

    private JetbrickTemplateUtils() {
    }

    public static String processTemplateIntoString(JetTemplate jetTemplate, Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        jetTemplate.render(context, writer);
        return writer.toString();
    }
}
