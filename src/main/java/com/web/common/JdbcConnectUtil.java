package com.web.common;
import java.sql.*;
public final class JdbcConnectUtil {
    private JdbcConnectUtil() {}
    public static String setting(String key, String fallback) {
        String value = System.getProperty(key, System.getenv(key));
        return value == null || value.isBlank() ? fallback : value;
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(setting("DB_URL", "jdbc:mysql://localhost:3306/pet_adoption?characterEncoding=UTF-8&serverTimezone=Asia/Seoul"), setting("DB_USER", "pet_app"), setting("DB_PASSWORD", ""));
    }
    public static void close(Connection con, PreparedStatement statement, ResultSet results) {
        for (AutoCloseable resource : new AutoCloseable[]{results, statement, con}) {
            if (resource != null) try { resource.close(); } catch (Exception e) { java.util.logging.Logger.getLogger(JdbcConnectUtil.class.getName()).warning(e.toString()); }
        }
    }
}
