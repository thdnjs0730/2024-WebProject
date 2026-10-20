package com.web.dao;

import com.web.common.JdbcConnectUtil;
import com.web.dto.MessageDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    // 메세지를 보내는 메서드
    public void sendMessage(MessageDto message) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "INSERT INTO messages (from_user_id, to_user_id, message) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, message.getFromUser());
            pstmt.setString(2, message.getToUser());
            pstmt.setString(3, message.getMessage());
            pstmt.executeUpdate();

        } finally {
            JdbcConnectUtil.close(con, pstmt, null);
        }
    }

    // 사용자에게 온 메세지를 조회하는 메서드
    public List<MessageDto> getMessagesForUser(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MessageDto> messages = new ArrayList<>();
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "SELECT * FROM messages WHERE to_user_id = ?";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String fromUser = rs.getString("from_user_id");
                String toUser = rs.getString("to_user_id");
                String messageContent = rs.getString("message");
                Timestamp createdAt = rs.getTimestamp("sent_at");

                MessageDto message = new MessageDto(fromUser, toUser, messageContent);
                message.setCreatedAt(createdAt);
                messages.add(message);
            }
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return messages;
    }
    public List<MessageDto> getSentMessages(String userId) throws SQLException {
        List<MessageDto> messages = new ArrayList<>();
        try (Connection con = JdbcConnectUtil.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM messages WHERE from_user_id = ? ORDER BY sent_at DESC, id DESC")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MessageDto m = new MessageDto(rs.getString("from_user_id"), rs.getString("to_user_id"), rs.getString("message"));
                    m.setCreatedAt(rs.getTimestamp("sent_at")); messages.add(m);
                }
            }
        }
        return messages;
    }
}
