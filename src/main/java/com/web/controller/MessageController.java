package com.web.controller;

import com.web.dao.MessageDao;
import com.web.dto.MessageDto;
import com.web.common.JdbcConnectUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet({"/sendMessage.do", "/messages.do"})
public class MessageController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/sendMessage.do".equals(action)) {
            handleSendMessage(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/messages.do".equals(action)) {
            handleListMessages(request, response);
        }
    }

    // 메시지 보내기 처리 메서드
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String fromUser = (String) session.getAttribute("userId");
        String toUser = request.getParameter("to");
        String messageContent = request.getParameter("message");

        if (fromUser == null) {
            response.sendRedirect("auth.jsp");
            return;
        }

        if (toUser == null || toUser.isBlank() || messageContent == null || messageContent.isBlank() || messageContent.length() > 5000 || !new com.web.dao.MemberDao().isIdDuplicate(toUser)) {
            response.sendError(400, "수신자와 쪽지 내용을 확인해 주세요."); return;
        }
        MessageDto message = new MessageDto(fromUser, toUser, messageContent);

        try {
            MessageDao messageDao = new MessageDao();
            messageDao.sendMessage(message);
            response.sendRedirect(request.getContextPath() + "/myPage.do");
        } catch (SQLException e) {
            throw new ServletException("쪽지 전송 실패", e);
        }
    }

    // 메시지 목록 조회 처리 메서드
    private void handleListMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("auth.jsp");
            return;
        }

        try {
            MessageDao messageDao = new MessageDao();
            List<MessageDto> messages = messageDao.getMessagesForUser(userId);
            request.setAttribute("messages", messages);
            response.sendRedirect(request.getContextPath() + "/myPage.do");
        } catch (SQLException e) {
            throw new ServletException("메시지 조회 중 오류 발생", e);
        }
    }
}
