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
            response.sendRedirect("loginForm.jsp"); 
            return;
        }

        MessageDto message = new MessageDto(fromUser, toUser, messageContent);

        try {
            MessageDao messageDao = new MessageDao();
            messageDao.sendMessage(message);
            response.sendRedirect("/adopt.jsp"); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메시지 목록 조회 처리 메서드
    private void handleListMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("loginForm.jsp"); 
            return;
        }

        try {
            MessageDao messageDao = new MessageDao(); 
            List<MessageDto> messages = messageDao.getMessagesForUser(userId); 
            request.setAttribute("messages", messages);
            request.getRequestDispatcher("/myPage.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("메시지 조회 중 오류 발생", e); 
        }
    }
}
