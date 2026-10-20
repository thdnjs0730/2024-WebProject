<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.web.dto.AdoptDto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>게시글 상세 정보</title>
    <link href="css/adoptForm.css" rel="stylesheet">
    <link rel="stylesheet" href="css/adopt.css">
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
    <%@include file="top.jsp" %>

    <!-- Page Content -->
    <div class="container mt-5">
            <%
                AdoptDto post = (AdoptDto) request.getAttribute("post");
                String userId = (String) request.getAttribute("id");                
            %>
        <form method="post" action="/deleteAdopt.do?postId=<%= post.getId() %>" >
        
            <%
                if (post != null) {
            %>
            <div class="mb-3">
                <h2 class="text-center"><%= post.getTitle() %></h2>
            </div>
            <div class="mb-3">
                <p class="form-label" id="user">작성자: <a href="/message.jsp?toUser=<%= post.getUserId() %>"><%= post.getNickname() %></a></p>
            </div>
            <div class="mb-3">
                <p class="form-label">작성일: <%= post.getCreatedAt() %></p>
            </div>
            <div class="mb-3">
                <p class="form-label">반려동물 종류: <%= post.getPetType() %></p>
            </div>
            <div class="mb-3">
                <p class="form-label"><img src="uploads/<%= post.getPhoto() %>" alt="<%= post.getTitle() %>" width="500px"></p>
            </div>
            <div class="mb-3">
                <p class="form-label"><%= post.getDescription() %></p>
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
	    		if(userId.equals(post.getUserId())) {
	    	%>
	    
				<a href="/editAdopt.do?postId=<%= post.getId() %>" class="button">수정</a>
		    	<button type="submit" class="button">삭제</button>
	    			
	    	<%	
	    		}
	    	%>
         </form>
        <% if (successMessage != null) { %>
    		<script type="text/javascript">
        		showSuccess('<%= successMessage %>');
    		</script>
    	<% } %>
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
