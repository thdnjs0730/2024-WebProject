package com.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.web.common.JdbcConnectUtil;
import com.web.dto.MemberDto;

public class MemberDao {

    // 로그인 확인
    public MemberDto loginCheck(String id, String password) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDto member = null;
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "SELECT id, password, is_admin FROM users WHERE id = ? AND password = ?";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new MemberDto();
                member.setId(rs.getString("id"));
                member.setPassword(rs.getString("password"));
                member.setAdmin(rs.getBoolean("is_admin"));
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return member;
    }

    // 회원 가입
    public boolean memberInsert(MemberDto mDTO) {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean flag = false;
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "INSERT INTO users (id, password, email, is_admin, nickname) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, mDTO.getId());
            pstmt.setString(2, mDTO.getPassword());
            pstmt.setString(3, mDTO.getEmail());
            pstmt.setBoolean(4, mDTO.isAdmin());
            pstmt.setString(5, mDTO.getNickname());

            int count = pstmt.executeUpdate();
            if (count == 1) {
                flag = true;
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            JdbcConnectUtil.close(con, pstmt, null);
        }
        return flag;
    }

    // 아이디 중복 체크
    public boolean isIdDuplicate(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isDuplicate = false;
        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                isDuplicate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return isDuplicate;
    }

    // 관리자 계정 초기 설정
    public void initializeAdmin() {
        // Explicit opt-in for the classroom demo; no built-in administrator password.
        String adminId = System.getenv("BOOTSTRAP_ADMIN_ID");
        String adminPassword = System.getenv("BOOTSTRAP_ADMIN_PASSWORD");
        if (adminId == null || adminId.isEmpty() || adminPassword == null || adminPassword.isEmpty()) {
            return;
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = JdbcConnectUtil.getConnection();
            
            // 관리자 계정이 이미 존재하는지 확인
            String checkSql = "SELECT COUNT(*) FROM users WHERE id = ?";
            pstmt = con.prepareStatement(checkSql);
            pstmt.setString(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return; // 관리자 계정이 이미 존재하는 경우
            }
            
            rs.close();
            pstmt.close();
            
            // 관리자 계정 생성
            String sql = "INSERT INTO users (id, password, email, is_admin, nickname) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, adminId); // 관리자 ID
            pstmt.setString(2, adminPassword); // 관리자 비밀번호
            pstmt.setString(3, "admin@example.invalid"); // 관리자 이메일
            pstmt.setBoolean(4, true); // 관리자 여부
            pstmt.setString(5, "관리자"); // 관리자 닉네임
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.close(con, pstmt, null);
        }
    }

    // 관리자 여부 확인
    public boolean isAdmin(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isAdmin = false;
        try {
            con = JdbcConnectUtil.getConnection();
            String strQuery = "SELECT is_admin FROM users WHERE id = ?";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                isAdmin = rs.getBoolean("is_admin");
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return isAdmin;
    }
    
    // 모든 회원 정보 가져오기
    public List<MemberDto> getAllMembers() {
        List<MemberDto> memberList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "SELECT id, password, email, is_admin, nickname FROM users";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MemberDto member = new MemberDto();
                member.setId(rs.getString("id"));
                member.setPassword(rs.getString("password"));
                member.setEmail(rs.getString("email"));
                member.setAdmin(rs.getBoolean("is_admin"));
                member.setNickname(rs.getString("nickname"));
                memberList.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return memberList;
    }
    
    // 회원 정보 업데이트
    public boolean updateMember(MemberDto member) {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean flag = false;
        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "UPDATE users SET password = ?, email = ?, nickname = ? WHERE id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getPassword());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getNickname());
            pstmt.setString(4, member.getId());

            int count = pstmt.executeUpdate();
            if (count == 1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.close(con, pstmt, null);
        }
        return flag;
    }
    
    // 회원 삭제
    public boolean deleteMember(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean flag = false;
        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "DELETE FROM users WHERE id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);

            int count = pstmt.executeUpdate();
            if (count == 1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.close(con, pstmt, null);
        }
        return flag;
    }
    
    // 닉네임이나 아이디로 회원 검색
    public List<MemberDto> searchMembers(String keyword) {
        List<MemberDto> memberList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "SELECT id, password, email, is_admin, nickname FROM users WHERE id LIKE ? OR nickname LIKE ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MemberDto member = new MemberDto();
                member.setId(rs.getString("id"));
                member.setPassword(rs.getString("password"));
                member.setEmail(rs.getString("email"));
                member.setAdmin(rs.getBoolean("is_admin"));
                member.setNickname(rs.getString("nickname"));
                memberList.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.close(con, pstmt, rs);
        }
        return memberList;
    }
}
