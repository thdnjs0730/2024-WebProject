package com.web.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.dao.AdoptDao;
import com.web.dao.MemberDao;
import com.web.dto.AdoptDto;
import com.web.dto.MemberDto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/adminPage.do")
public class AdminController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("searchMembers".equals(action)) {
            searchMembers(request, response);
        } else if ("searchPosts".equals(action)) {
            searchPosts(request, response);
        } else {
            // 기본적으로 모든 회원과 게시글을 가져옴
            MemberDao memberDao = new MemberDao();
            AdoptDao adoptDao = new AdoptDao();
            List<MemberDto> members = new ArrayList<>();
            List<AdoptDto> posts = new ArrayList<>();
            try {
                members = memberDao.getAllMembers();
                posts = adoptDao.getAllPosts();
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "데이터를 불러오는 중 오류가 발생했습니다.");
            }
            request.setAttribute("members", members);
            request.setAttribute("posts", posts);
            request.setAttribute("activeTab", "member"); // 기본적으로 회원 관리 탭 활성화

            // admin.jsp로 포워딩
            RequestDispatcher dispatcher = request.getRequestDispatcher("admin.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void searchMembers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        MemberDao memberDao = new MemberDao();
        List<MemberDto> members = new ArrayList<>();

        try {
            // 검색 키워드가 있는 경우와 없는 경우로 나누어 회원 목록을 가져옴
            if (keyword != null && !keyword.trim().isEmpty()) {
                members = memberDao.searchMembers(keyword);
            } else {
                members = memberDao.getAllMembers();
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "회원 정보를 불러오는 중 오류가 발생했습니다.");
        }

        request.setAttribute("members", members);
        request.setAttribute("activeTab", "member");

        // admin.jsp로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin.jsp");
        dispatcher.forward(request, response);
    }

    private void searchPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        AdoptDao adoptDao = new AdoptDao();
        List<AdoptDto> posts = new ArrayList<>();

        try {
            // 검색 키워드가 있는 경우와 없는 경우로 나누어 게시글 목록을 가져옴
            if (keyword != null && !keyword.trim().isEmpty()) {
                posts = adoptDao.searchPostsByTitle(keyword);
            } else {
                posts = adoptDao.getAllPosts();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "게시글 정보를 불러오는 중 오류가 발생했습니다.");
        }

        request.setAttribute("posts", posts);
        request.setAttribute("activeTab", "post");

        // admin.jsp로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");

        MemberDao memberDao = new MemberDao();
        AdoptDao adoptDao = new AdoptDao();

        if (id == null || id.isBlank() || ("DeleteMember".equals(action) && memberDao.isAdmin(id))) { response.sendError(400); return; }
        if (!"DeleteMember".equals(action) && !"DeletePost".equals(action)) { response.sendError(400); return; }
        // action이 "DeleteMember"인 경우 회원 삭제 처리
        if ("DeleteMember".equals(action)) {
            boolean deleteResult = memberDao.deleteMember(id);
            if (deleteResult) {
                request.setAttribute("successMessage", "회원이 성공적으로 삭제되었습니다.");
            } else {
                request.setAttribute("errorMessage", "회원 삭제에 실패했습니다.");
            }
            request.setAttribute("activeTab", "member");
        }

        // action이 "DeletePost"인 경우 게시글 삭제 처리
        if ("DeletePost".equals(action)) {
            if (id != null && !id.trim().isEmpty()) {
                try {
                    // 삭제하기 전에 게시글이 존재하는지 확인
                    AdoptDto post = adoptDao.getPostById(Integer.parseInt(id));
                    if (post != null) {
                        adoptDao.deletePost(Integer.parseInt(id));
                        request.setAttribute("successMessage", "게시글이 성공적으로 삭제되었습니다.");
                    } else {
                        request.setAttribute("errorMessage", "해당 게시글이 존재하지 않습니다.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "게시글 삭제 중 오류가 발생했습니다.");
                }
            } else {
                request.setAttribute("errorMessage", "유효하지 않은 게시글 ID입니다.");
            }
            request.setAttribute("activeTab", "post");
        }

        // 수정 후 변경된 목록을 다시 가져와서 표시
        try {
            request.setAttribute("members", memberDao.getAllMembers());
            request.setAttribute("posts", adoptDao.getAllPosts());
        } catch (SQLException e) { throw new ServletException(e); }
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }
}
