<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/top.css">
</head>
<body class="top-body">
<!-- 헤더 섹션 -->
    <header class="top-header">
        <%
            request.getSession();
            String mem_id = (String) session.getAttribute("loginResult");
            Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
            if (isAdmin == null) {
                isAdmin = false;
            }

            String log = "";
            if (mem_id == null) {
                log = "<a href='loginForm.jsp'> 로그인 </a>";
            } else {
                log = "<a href='logout.do'> 로그아웃 </a>";
            }

            String mem = "";
            if (mem_id == null) {
                mem = "<a href='loginForm.jsp?mode=signup'> 회원가입 </a>";
            } else {
                if (isAdmin) {
                    mem = "<a href='adminPage.jsp'> 관리자 페이지 </a>";
                } else {
                    mem = "<a href='myPage.do'> 마이페이지 </a>";
                }
            }

            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null && !successMessage.isEmpty()) {
                session.removeAttribute("successMessage");
        %>
        <script type="text/javascript">
            showSuccess('<%= successMessage %>');
        </script>
        <%
            }
        %>
        <div class="top-nav">
            <ul class="top-nav-right">
                <li><a href="index.jsp">HOME</a></li>
                <li><a><%= log %></a></li>
                <li><a><%= mem %></a></li>
            </ul>
        </div>
        <div class="top-main-nav">
            <nav>
                <ul class="top-nav-left">
                    <li><a href="index.jsp"><img src="images/logo-black.png" alt="Logo"></a></li>
                    <li><a href="intro.jsp" class="underline">소개</a></li>
                    <li><a href="adopt.jsp" class="underline">입양</a></li>
                    <li><a href="#" class="underline">참여</a></li>
                    <li><a href="#" class="underline">후원</a></li>
                </ul>
            </nav>
        </div>
    </header>
</body>
</html>
