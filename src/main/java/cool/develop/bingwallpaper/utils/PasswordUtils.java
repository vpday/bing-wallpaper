package cool.develop.bingwallpaper.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author vpday
 * @create 2019/4/13
 */
@Slf4j
public final class PasswordUtils {

    public static String hashPassword(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] bytes = md.digest(plaintext.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            log.error(SiteUtils.getStackTrace(e));
            return "";
        }
    }

    public static boolean checkPassword(String plaintext, String hashed) {
        if (null == hashed) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }

        byte[] hashedBytes;
        byte[] tryBytes;
        String tryPw = hashPassword(plaintext);

        hashedBytes = hashed.getBytes(StandardCharsets.UTF_8);
        tryBytes = tryPw.getBytes(StandardCharsets.UTF_8);
        if (hashedBytes.length != tryBytes.length) {
            return false;
        }

        byte ret = 0;
        for (int i = 0; i < tryBytes.length; i++) {
            ret |= hashedBytes[i] ^ tryBytes[i];
        }
        return ret == 0;
    }
}
