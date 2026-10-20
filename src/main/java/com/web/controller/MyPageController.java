package com.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.web.dao.MyPageDao;
import com.web.dto.MyPageDto;

@WebServlet({"/myPage.do", "/edit.do"})
@MultipartConfig
public class MyPageController extends HttpServlet {
    private MyPageDao myPageDao;

    @Override
    public void init() {
        myPageDao = new MyPageDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getServletPath();
        if ("/edit.do".equals(action)) {
            handleEdit(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        if ("/myPage.do".equals(action)) {
            handleMyInformation(request, response);
        }
    }

    // 마이페이지 정보를 처리하는 메서드
    public void handleMyInformation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("auth.jsp");
            return;
        }

        MyPageDto myPageDto = new MyPageDto();
        try {
            myPageDto = myPageDao.getMyInformation(userId);
        } catch (SQLException e) {
            throw new ServletException("정보 조회 중 오류 발생", e);
        }


        request.setAttribute("email", myPageDto.getEmail());
        request.setAttribute("username", myPageDto.getUsername());
        request.setAttribute("id", userId);
        request.getRequestDispatcher("/account.jsp").forward(request, response);
    }

    // 정보 수정 처리를 하는 메서드
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("loginResult", 0);
            RequestDispatcher dispatcher = request.getRequestDispatcher("auth.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String password = request.getParameter("edit-password");
        String email = request.getParameter("edit-email");
        String username = request.getParameter("edit-username");

        MyPageDto myPageDto = new MyPageDto();
        if (email == null || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+") || email.length() > 254 || username == null || username.isBlank() || username.length() > 45 || (password != null && !password.isBlank() && !com.web.common.Passwords.valid(password))) {
            response.sendError(400, "입력값을 확인해 주세요."); return;
        }
        myPageDto.setEmail(email);
        myPageDto.setPassword(password);
        myPageDto.setUsername(username);

        try {
            myPageDao.updateMyInformation(userId, myPageDto);
        } catch (SQLException e) {
            throw new ServletException("정보 수정 중 오류 발생", e);
        }

        response.sendRedirect(request.getContextPath() + "/myPage.do");
    }
}
