<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // 세션에서 로그인 상태를 나타내는 속성 제거
    session.removeAttribute("loginResult");
    // 홈 페이지로 리다이렉트
    response.sendRedirect("index.jsp");
%>
