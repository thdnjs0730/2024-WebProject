<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.web.dto.AdoptDto" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>입양 게시글 수정</title>
    <link href="css/adoptForm.css" rel="stylesheet">
</head>
<body>
<%
	AdoptDto post = (AdoptDto) request.getAttribute("post");
%>
    <!-- Navigation -->
    <%@include file="top.jsp" %>

    <!-- Page Content -->
    <div class="container mt-5">
        <h1 class="text-center">입양 게시글 수정</h1>
        <form action="/editAdoptForm.do?postId=<%= post.getId() %>" method="post" enctype="multipart/form-data" id="adoptForm">
            <div class="mb-3">
                <label for="title" class="form-label">제목:</label>
                <input type="text" id="title" name="title" class="form-control" placeholder="제목을 입력하세요." value=<%=post.getTitle() %> required>
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">내용:</label>
                <textarea id="description" name="description" class="form-control" rows="5" placeholder="내용을 입력하세요." required><%=post.getDescription() %></textarea>
            </div>
            <div class="mb-3">
                <label for="petType" class="form-label" value=<%=post.getPetType() %>>반려동물 종류:</label>
                <select id="petType" name="pet_type" class="form-control" required>
                    <option value="" disabled selected>반려동물 종류를 선택하세요.</option>
                    <option value="강아지">강아지</option>
                    <option value="고양이">고양이</option>
                    <option value="기타">기타동물</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="photo" class="form-label">사진 업로드:</label>
                <input type="file" id="photo" name="photo" class="form-control" value=<%=post.getPhoto() %>required>
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
        	window.location.href = "adopt.jsp";
        }
    </script>
</body>
</html>
