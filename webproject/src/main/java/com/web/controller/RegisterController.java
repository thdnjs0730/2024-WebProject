package com.web.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.web.dao.MemberDao;
import com.web.dto.MemberDto;

@WebServlet({"/register.do", "/login.do", "/logout.do"})
public class RegisterController extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        // 서블릿 초기화 코드 (필요시 추가)
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        if ("/logout.do".equals(action)) {
            handleLogout(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/register.do".equals(action)) {
            handleRegister(request, response);
        } else if ("/login.do".equals(action)) {
            handleLogin(request, response);
        }
    }
    
    // 회원가입 처리 메서드
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String pw = request.getParameter("password");
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");

        MemberDao mDao = new MemberDao();

        // 아이디 중복 체크
        if (mDao.isIdDuplicate(id)) {
            request.setAttribute("errorMessage", "이미 존재하는 아이디입니다.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("loginForm.jsp?mode=signup");
            dispatcher.forward(request, response);
            return;
        }

        MemberDto mDto = new MemberDto();
        mDto.setId(id);
        mDto.setPassword(pw);
        mDto.setEmail(email);
        mDto.setNickname(nickname);

        // 회원가입 처리
        boolean insertCheck = mDao.memberInsert(mDto);

        if (insertCheck) {
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "회원가입이 성공적으로 완료되었습니다.");
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("joinResult", 0);
            RequestDispatcher dispatcher = request.getRequestDispatcher("loginForm.jsp");
            dispatcher.forward(request, response);
        }
    }

    // 로그인 처리 메서드
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String pw = request.getParameter("password");

        MemberDao mDao = new MemberDao();
        MemberDto member = mDao.loginCheck(id, pw);

        if (member != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", member.getId());  // userId를 그대로 저장
            session.setAttribute("loginResult", member.getId());
            session.setAttribute("isAdmin", member.isAdmin());
            System.out.println("User ID stored in session: " + member.getId()); // 로그 추가
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("loginResult", 0);
            RequestDispatcher dispatcher = request.getRequestDispatcher("loginForm.jsp");
            dispatcher.forward(request, response);
        }
    }

    // 로그아웃 처리 메서드
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        response.sendRedirect("index.jsp"); // 로그아웃 후 메인 페이지로 리디렉션
    }
}
