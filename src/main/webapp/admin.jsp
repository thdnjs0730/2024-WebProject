<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% if (request.getAttribute("activeTab") == null) { response.sendRedirect(request.getContextPath() + "/adminPage.do"); return; } %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>관리자 페이지</title>
<link rel="stylesheet" href="assets/css/admin.css"><style>body{font-family:sans-serif;padding:24px}table{width:100%;border-collapse:collapse;margin-bottom:36px}td,th{border:1px solid #ddd;padding:12px}form{margin:12px 0}input,button{padding:8px}</style></head>
<body><nav><a href="index.jsp">홈으로</a> · <a href="adminPage.do">전체 목록</a></nav><h1>관리자 페이지</h1>
<p role="status"><c:out value="${successMessage}"/></p><p role="alert"><c:out value="${errorMessage}"/></p>
<h2>회원 관리</h2><form action="adminPage.do" method="get"><input type="hidden" name="action" value="searchMembers"><input name="keyword" placeholder="아이디 또는 닉네임"><button>회원 검색</button></form>
<table><thead><tr><th>아이디</th><th>이메일</th><th>닉네임</th><th>관리</th></tr></thead><tbody>
<c:forEach var="member" items="${members}"><tr><td><c:out value="${member.id}"/></td><td><c:out value="${member.email}"/></td><td><c:out value="${member.nickname}"/></td><td>
<c:if test="${!member.admin}"><form action="adminPage.do" method="post"><input type="hidden" name="id" value="<c:out value='${member.id}'/>"><button name="action" value="DeleteMember">회원 삭제</button></form></c:if></td></tr></c:forEach>
</tbody></table>
<h2>게시글 관리</h2><form action="adminPage.do" method="get"><input type="hidden" name="action" value="searchPosts"><input name="keyword" placeholder="게시글 제목"><button>게시글 검색</button></form>
<table><thead><tr><th>제목</th><th>작성자</th><th>작성일</th><th>관리</th></tr></thead><tbody>
<c:forEach var="post" items="${posts}"><tr><td><a href="adoptDetail.do?postId=${post.id}"><c:out value="${post.title}"/></a></td><td><c:out value="${post.userId}"/></td><td><c:out value="${post.createdAt}"/></td><td>
<form action="adminPage.do" method="post"><input type="hidden" name="id" value="${post.id}"><button name="action" value="DeletePost">게시글 삭제</button></form></td></tr></c:forEach>
</tbody></table></body></html>
