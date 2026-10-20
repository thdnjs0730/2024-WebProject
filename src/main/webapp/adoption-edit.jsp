<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.web.dto.AdoptDto" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>입양 게시글 수정</title>
    <link href="assets/css/adoption-form.css" rel="stylesheet">
</head>
<body>
<%
    AdoptDto post = (AdoptDto) request.getAttribute("post");
    if (post == null) { response.sendError(404); return; }
%>
    <!-- Navigation -->
    <%@include file="/WEB-INF/fragments/header.jspf" %>

    <!-- Page Content -->
    <div class="container mt-5">
        <h1 class="text-center">입양 게시글 수정</h1>
        <form action="<%= request.getContextPath() %>/editAdoptForm.do?postId=<%= com.web.common.Html.escape(post.getId()) %>" method="post" enctype="multipart/form-data" id="adoptForm">
            <div class="mb-3">
                <label for="title" class="form-label">제목:</label>
                <input type="text" id="title" name="title" class="form-control" placeholder="제목을 입력하세요." value="<%= com.web.common.Html.escape(post.getTitle()) %>" required>
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">내용:</label>
                <textarea id="description" name="description" class="form-control" rows="5" placeholder="내용을 입력하세요." required><%= com.web.common.Html.escape(post.getDescription()) %></textarea>
            </div>
            <div class="mb-3">
                <label for="petType" class="form-label">반려동물 종류:</label>
                <select id="petType" name="pet_type" class="form-control" required>
                    <option value="" disabled>반려동물 종류를 선택하세요.</option>
                    <option value="강아지" <%= "강아지".equals(post.getPetType()) ? "selected" : "" %>>강아지</option>
                    <option value="고양이" <%= "고양이".equals(post.getPetType()) ? "selected" : "" %>>고양이</option>
                    <option value="기타" <%= "기타".equals(post.getPetType()) ? "selected" : "" %>>기타동물</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="photo" class="form-label">사진 업로드:</label>
                <input type="file" id="photo" name="photo" class="form-control" accept="image/png,image/jpeg,image/gif">
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">수정</button>
            </div>
        </form>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"></script>

    <script>
        function resetForm() {
            document.getElementById("adoptForm").reset();
        }

        function cancelForm() {
            window.location.href = "adoption-list.jsp";
        }
    </script>
</body>
</html>
