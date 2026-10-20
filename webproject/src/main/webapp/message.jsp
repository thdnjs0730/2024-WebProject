<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.*" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/myPage.css">
    <title>쪽지 보내기</title>
</head>
<body>
<%@ include file="top.jsp" %>
<div class="container">
    <div class="my-page-container">
        <h2 class="title">쪽지 보내기</h2>
        <form action="/sendMessage.do" method="post">
            <div class="input-field">
                <label for="to">To:</label>
                <input type="text" id="to" name="to" value="<%= request.getParameter("toUser") %>" readonly>
            </div>
            <div class="input-field">
                <label for="message">Message:</label>
                <textarea id="message" name="message" rows="4" required></textarea>
            </div>
            <button class="btn" type="submit">전송</button>
        </form>
    </div>
</div>
</body>
</html>
