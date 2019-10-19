package cool.develop.bingwallpaper.bootstrap;

import com.blade.mvc.Const;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author vpday
 * @date 2018/11/23
 */
@Slf4j
class SqliteJdbc {

    private SqliteJdbc() {
    }

    private static final String DB_NAME = "bing-wallpaper.db";
    private static final String IS_NEW_DB_SQL = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name IN ('t_bing_wallpaper','t_filming_location')";
    private static String dbSrc;
    private static Boolean isNewDb;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            log.error("load sqlite driver error", e);
        }
    }

    static String dbSrc() {
        return dbSrc;
    }

    static Boolean isNewDb() {
        return isNewDb;
    }

    /**
     * 测试连接并导入数据库
     */
    static void importSql(boolean devMode) {
        String dbPath = Const.CLASSPATH + File.separatorChar + DB_NAME;
        dbSrc = "jdbc:sqlite://" + dbPath;

        if (devMode) {
            dbPath = System.getProperty("user.dir") + "/" + DB_NAME;
            dbSrc = "jdbc:sqlite://" + dbPath;
        }

        log.info("load sqlite database path [{}]", dbPath);
        log.info("load sqlite database src [{}]", dbSrc);

        try (Connection con = DriverManager.getConnection(dbSrc);
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(IS_NEW_DB_SQL)) {

            isNewDb = 0 == rs.getInt(1);
            if (Boolean.TRUE.equals(isNewDb)) {
                String cp = Objects.requireNonNull(SqliteJdbc.class.getClassLoader().getResource("")).getPath();
                try (InputStreamReader isr = new InputStreamReader(new FileInputStream(cp + "schema.sql"), StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(isr)) {
                    String sql = bufferedReader.lines().collect(Collectors.joining("\n"));
                    int r = statement.executeUpdate(sql);
                    log.info("initialize import database - {}", r);
                }
            }

            log.info("database path is: {}", dbPath);
        } catch (Exception e) {
            log.error("initialize database fail", e);
        }
    }
}
