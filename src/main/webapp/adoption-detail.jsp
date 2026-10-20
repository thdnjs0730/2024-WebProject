<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.web.dto.AdoptDto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>게시글 상세 정보</title>
    <link href="assets/css/adoption-form.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/adoption-list.css">
    <style>
        .text-center {
            color: #ff944d;
            text-decoration: none;
        }
        #user {
            text-decoration: none;
        }
    </style>
</head>
<body>
<!-- Navigation -->
    <%@include file="/WEB-INF/fragments/header.jspf" %>

    <!-- Page Content -->
    <div class="container mt-5">
            <%
                AdoptDto post = (AdoptDto) request.getAttribute("post");
                if (post == null) { response.sendError(404); return; }
                String userId = (String) request.getAttribute("id");
            %>
        <form method="post" action="<%= request.getContextPath() %>/deleteAdopt.do?postId=<%= com.web.common.Html.escape(post.getId()) %>" >

            <%
                if (post != null) {
            %>
            <div class="mb-3">
                <h2 class="text-center"><%= com.web.common.Html.escape(post.getTitle()) %></h2>
            </div>
            <div class="mb-3">
                <p class="form-label" id="user">작성자: <a href="<%= request.getContextPath() %>/message-compose.jsp?toUser=<%= com.web.common.Html.escape(post.getUserId()) %>"><%= com.web.common.Html.escape(post.getNickname()) %></a></p>
            </div>
            <div class="mb-3">
                <p class="form-label">작성일: <%= com.web.common.Html.escape(post.getCreatedAt()) %></p>
            </div>
            <div class="mb-3">
                <p class="form-label">반려동물 종류: <%= com.web.common.Html.escape(post.getPetType()) %></p>
            </div>
            <div class="mb-3">
                <p class="form-label"><img src="uploads/<%= com.web.common.Html.escape(post.getPhoto()) %>" alt="<%= com.web.common.Html.escape(post.getTitle()) %>" width="500px"></p>
            </div>
            <div class="mb-3">
                <p class="form-label"><%= com.web.common.Html.escape(post.getDescription()) %></p>
            </div>
            <%
                } else {
            %>

                <p>게시글을 찾을 수 없습니다.</p>
            <%
                }
            %>

        <a class="back" href="adopt">목록으로 돌아가기</a>
        <%
                if(userId != null && userId.equals(post.getUserId())) {
            %>

                <a href="<%= request.getContextPath() %>/editAdopt.do?postId=<%= com.web.common.Html.escape(post.getId()) %>" class="button">수정</a>
                <button type="submit" class="button">삭제</button>

            <%
                }
            %>
         </form>

    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"></script>

</body>
<script type="text/javascript">
        function showSuccess(message) {
            alert(message);
        }
    </script>
</html>
