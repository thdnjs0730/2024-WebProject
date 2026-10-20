<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page session="true" %>

<%
    String mode = request.getParameter("mode");
    boolean isSignupMode = "signup".equals(mode);
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/auth.css">
    <title>로그인</title>
    <script type="text/javascript">
        function showError(message) {
            alert(message);
        }
        function showSuccess(message) {
            alert(message);
        }
    </script>
</head>
<body>
    <div class="container <%= isSignupMode ? "sign-up-mode" : "" %>">
        <!-- 로그인 및 회원가입 폼 내용 -->
        <div class="forms-container">
            <div class="signin-signup">
            <!-- 로그인 -->
                <form action="login.do" method="post" class="sign-in-form">
                    <a class="home" href="index.jsp">홈으로</a>
                    <h2 class="title">Sign in</h2>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="text" placeholder="Username" name="id" required maxlength="32"/>
                    </div>
                    <div class="input-field">
                        <i class="fas fa-lock"></i>
                        <input type="password" placeholder="Password" name="password" required/>
                    </div>
                    <input type="submit" value="Login" class="btn solid" />
                </form>
            <!-- 회원가입 -->
                <form action="register.do" method="post" class="sign-up-form">
                    <a class="home" href="index.jsp">홈으로</a>
                    <h2 class="title">Sign up</h2>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="text" placeholder="Username" name="id" required maxlength="32"/>
                    </div>
                    <div class="input-field">
                        <i class="fas fa-lock"></i>
                        <input type="password" placeholder="Password" name="password" required/>
                    </div>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="text" placeholder="Nickname" name="nickname" required maxlength="45"/>
                    </div>
                    <div class="input-field">
                        <i class="fas fa-envelope"></i>
                        <input type="email" placeholder="Email" name="email" required maxlength="254"/>
                    </div>
                    <input type="submit" class="btn" value="Sign up" />
                </form>
            </div>
        </div>
        <div class="panels-container">
            <div class="panel left-panel">
                <div class="content">
                    <h3>새로 오셨나요?</h3>
                    <p>회원가입 하세요.</p>
                    <button class="btn transparent" id="sign-up-btn">회원가입</button>

                </div>
            </div>
            <div class="panel right-panel">
                <div class="content">
                    <h3>이미 회원이신가요?</h3>
                    <p>로그인 하세요.</p>
                    <button class="btn transparent" id="sign-in-btn">로그인</button>
                </div>
            </div>
        </div>
    </div>
    <script src="assets/js/auth.js"></script>
    <% if (errorMessage != null) { %><p role="alert"><%= com.web.common.Html.escape(errorMessage) %></p><% } %>
    <% if (successMessage != null) { %><p role="status"><%= com.web.common.Html.escape(successMessage) %></p><% } %>
</body>
</html>
