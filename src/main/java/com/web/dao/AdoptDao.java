package com.web.dao;

import com.web.common.JdbcConnectUtil;
import com.web.dto.AdoptDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdoptDao {

    // 새 입양 게시글을 삽입하는 메서드
    public void insertPost(AdoptDto post) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = JdbcConnectUtil.getConnection();
            String sql = "INSERT INTO adoptionposts (title, description, pet_type, user_id, photo) VALUES (?, ?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getDescription());
            pstmt.setString(3, post.getPetType());
            pstmt.setString(4, post.getUserId());
            pstmt.setString(5, post.getPhoto());
            pstmt.executeUpdate();
        } finally {
            JdbcConnectUtil.close(connection, pstmt, null);
        }
    }

    // 모든 입양 게시글을 가져오는 메서드
    public List<AdoptDto> getAllPosts() throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptDto> posts = new ArrayList<>();

        try {
            connection = JdbcConnectUtil.getConnection();
            if (connection != null) {
                String sql = "SELECT a.id, a.title, a.description, a.pet_type, a.created_at, a.photo, u.nickname, a.user_id " +
                             "FROM adoptionposts a " +
                             "JOIN users u ON a.user_id = u.ID " +
                             "ORDER BY a.created_at DESC";
                pstmt = connection.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    AdoptDto post = new AdoptDto();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setDescription(rs.getString("description"));
                    post.setPetType(rs.getString("pet_type"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setPhoto(rs.getString("photo"));
                    post.setNickname(rs.getString("nickname"));
                    post.setUserId(rs.getString("user_id"));
                    posts.add(post);
                }
            } else {
                System.out.println("Failed to make connection!");
            }
        } finally {
            JdbcConnectUtil.close(connection, pstmt, rs);
        }
        return posts;
    }

    // ID로 특정 입양 게시글을 가져오는 메서드
    public AdoptDto getPostById(int postId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AdoptDto post = null;

        try {
            connection = JdbcConnectUtil.getConnection();
            String sql = "SELECT a.id, a.title, a.description, a.pet_type, a.created_at, a.photo, u.nickname, a.user_id " +
                         "FROM adoptionposts a " +
                         "JOIN users u ON a.user_id = u.ID " +
                         "WHERE a.id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                post = new AdoptDto();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setDescription(rs.getString("description"));
                post.setPetType(rs.getString("pet_type"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setPhoto(rs.getString("photo"));
                post.setNickname(rs.getString("nickname"));
                post.setUserId(rs.getString("user_id"));
            }
        } finally {
            JdbcConnectUtil.close(connection, pstmt, rs);
        }
        return post;
    }

    // 최근 n개의 입양 게시글을 가져오는 메서드
    public List<AdoptDto> getLatestPosts(int limit) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptDto> posts = new ArrayList<>();

        try {
            connection = JdbcConnectUtil.getConnection();
            if (connection != null) {
                String sql = "SELECT a.id, a.title, a.description, a.pet_type, a.created_at, a.photo, u.nickname " +
                             "FROM adoptionposts a " +
                             "JOIN users u ON a.user_id = u.ID " +
                             "ORDER BY a.created_at DESC LIMIT ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, limit);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    AdoptDto post = new AdoptDto();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setDescription(rs.getString("description"));
                    post.setPetType(rs.getString("pet_type"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setPhoto(rs.getString("photo"));
                    post.setNickname(rs.getString("nickname"));
                    posts.add(post);
                }
            } else {
                System.out.println("Failed to make connection!");
            }
        } finally {
            JdbcConnectUtil.close(connection, pstmt, rs);
        }
        return posts;
    }

    // 검색 기능
    public List<AdoptDto> searchPosts(String searchQuery, String searchType, String petType) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptDto> posts = new ArrayList<>();

        try {
            connection = JdbcConnectUtil.getConnection();
            if (connection != null) {
                StringBuilder sql = new StringBuilder("SELECT a.id, a.title, a.description, a.pet_type, a.created_at, a.photo, u.nickname " +
                                                      "FROM adoptionposts a " +
                                                      "JOIN users u ON a.user_id = u.ID " +
                                                      "WHERE 1=1 ");

                // 검색 조건 추가
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    if (!"nickname".equals(searchType)) {
                        sql.append("AND a.title LIKE ? ");
                    } else if ("nickname".equals(searchType)) {
                        sql.append("AND u.nickname LIKE ? ");
                    }
                }

                if (petType != null && !petType.isEmpty()) {
                    sql.append("AND a.pet_type = ? ");
                }

                sql.append("ORDER BY a.created_at DESC");

                pstmt = connection.prepareStatement(sql.toString());

                // 파라미터 설정
                int paramIndex = 1;
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + searchQuery + "%");
                }
                if (petType != null && !petType.isEmpty()) {
                    pstmt.setString(paramIndex++, petType);
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    AdoptDto post = new AdoptDto();
                    post.setNickname(rs.getString("nickname"));
                    post.setTitle(rs.getString("title"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setPhoto(rs.getString("photo"));
                    post.setId(rs.getInt("id"));
                    posts.add(post);
                }
            } else {
                System.out.println("Failed to make connection!");
            }
        } finally {
            JdbcConnectUtil.close(connection, pstmt, rs);
        }
        return posts;
    }

    // 제목으로 게시글 검색
    public List<AdoptDto> searchPostsByTitle(String searchQuery) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AdoptDto> posts = new ArrayList<>();

        try {
            connection = JdbcConnectUtil.getConnection();
            if (connection != null) {
                String sql = "SELECT a.id, a.title, a.created_at, a.user_id " +
                             "FROM adoptionposts a " +
                             "JOIN users u ON a.user_id = u.ID " +
                             "WHERE a.title LIKE ? " +
                             "ORDER BY a.created_at DESC";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, "%" + searchQuery + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    AdoptDto post = new AdoptDto();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setUserId(rs.getString("user_id"));
                    posts.add(post);
                }
            } else {
                System.out.println("Failed to make connection!");
            }
        } finally {
            JdbcConnectUtil.close(connection, pstmt, rs);
        }
        return posts;
    }

    // 입양 게시글을 업데이트하는 메서드
    public void updatePost(AdoptDto post) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = JdbcConnectUtil.getConnection();
            String sql = "UPDATE adoptionposts SET title = ?, description = ?, pet_type = ?, photo = ? WHERE id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getDescription());
            pstmt.setString(3, post.getPetType());
            pstmt.setString(4, post.getPhoto());
            pstmt.setInt(5, post.getId());
            pstmt.executeUpdate();
        } finally {
            JdbcConnectUtil.close(connection, pstmt, null);
        }
    }

    // 입양 게시글을 삭제하는 메서드
    public void deletePost(int postId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = JdbcConnectUtil.getConnection();
            String sql = "DELETE FROM adoptionposts WHERE id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } finally {
            JdbcConnectUtil.close(connection, pstmt, null);
        }
    }
}
