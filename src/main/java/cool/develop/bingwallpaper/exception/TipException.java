package cool.develop.bingwallpaper.exception;

/**
 * @author vpday
 * @create 2018/11/23
 */
public class TipException extends RuntimeException {

    private static final long serialVersionUID = -1677077667401999981L;

    public TipException() {
    }

    public TipException(String message) {
        super(message);
    }

    public TipException(Throwable cause) {
        super(cause);
    }
}
