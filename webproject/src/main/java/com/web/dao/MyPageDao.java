package com.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.web.common.JdbcConnectUtil;
import com.web.dto.AdoptDto;
import com.web.dto.MyPageDto;

public class MyPageDao {

    static MyPageDto myPage = new MyPageDto();

    // 사용자 정보 조회 메서드
    public MyPageDto getMyInformation(String id) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "SELECT password, email, nickname FROM users WHERE id = ?";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                myPage.setPassword(rs.getString("password"));
                myPage.setEmail(rs.getString("email"));
                myPage.setUsername(rs.getString("nickname"));
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return myPage;
    }

    // 사용자가 작성한 입양 게시글 조회 메서드
    public List<AdoptDto> getMyPosts(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptDto> posts = new ArrayList<>();
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "SELECT a.id, a.title, a.description, a.pet_type, a.created_at, a.photo, u.nickname " +
                              "FROM adoptionposts a " +
                              "JOIN users u ON a.user_id = u.ID " +
                              "WHERE a.user_id = ? " +
                              "ORDER BY a.created_at DESC";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AdoptDto post = new AdoptDto(); // 반복문 내에서 새로 생성
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setDescription(rs.getString("description"));
                post.setPetType(rs.getString("pet_type"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setPhoto(rs.getString("photo"));
                post.setNickname(rs.getString("nickname"));
                posts.add(post);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return posts;
    }

    // 사용자 정보 업데이트 메서드
    public void updateMyInformation(String id, MyPageDto myPageDto) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String password = myPageDto.getPassword();
        String email = myPageDto.getEmail();
        String nickname = myPageDto.getUsername();
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "UPDATE USERS SET password = ?, email = ?, nickname = ? WHERE id = ?";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, password);
            pstmt.setString(2, email);
            pstmt.setString(3, nickname);
            pstmt.setString(4, id);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
    }
}
