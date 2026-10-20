package com.web.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcConnectUtil {
    
    private static JdbcConnectUtil mDao;

    private PreparedStatement pstmt;
    private ResultSet rs;
    private int result;

    // 데이터베이스 연결을 위한 메서드
    public static Connection getConnection() {
        // 데이터베이스 연결 정보
        String url = System.getenv("DB_URL");
        String id = System.getenv("DB_USER");
        String pw = System.getenv("DB_PASSWORD");
        if (url == null || id == null || pw == null) {
            throw new IllegalStateException("DB_URL, DB_USER, DB_PASSWORD 환경변수가 필요합니다.");
        }
        
        try {
            // JDBC 드라이버를 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, id, pw);
        } catch (ClassNotFoundException e) {
            // JDBC 드라이버 로드에 실패한 경우 예외 메시지를 출력
            e.printStackTrace();
            System.out.println("JDBC 드라이버 로드 실패: " + e.getMessage());
        } catch (SQLException e) {
            // 데이터베이스 연결에 실패한 경우 예외 메시지를 출력
            e.printStackTrace();
            System.out.println("데이터베이스 연결 실패: " + e.getMessage());
        }
        // 연결에 실패한 경우 null을 반환
        return null;
    }

    // 데이터베이스 리소스들을 닫는 메서드
    public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        // ResultSet을 닫음
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // PreparedStatement를 닫음
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Connection을 닫음
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
