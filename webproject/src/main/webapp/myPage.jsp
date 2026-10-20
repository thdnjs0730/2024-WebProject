<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.web.dto.AdoptDto" %>
<%@ page import="com.web.dto.MessageDto" %>
<%@ page import="com.web.dao.MyPageDao" %>
<%@ page import="com.web.dao.MessageDao" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/myPage.css">
    <title>마이페이지</title>
</head>
<body>
<%@ include file="top.jsp" %>
<%
    String password = (String) request.getAttribute("password");
    String email = (String) request.getAttribute("email");
    String username = (String) request.getAttribute("username");
    String id = (String) request.getAttribute("id");
    MyPageDao myPageDao = new MyPageDao();
    MessageDao messageDao = new MessageDao();
    List<AdoptDto> posts = null;
    List<MessageDto> messages = null;
    try {
        posts = myPageDao.getMyPosts(id);
        messages = messageDao.getMessagesForUser(id);
    } catch (Exception e) {
        e.printStackTrace();
    }
    
%>
<div class="container">
    <!-- 마이페이지 내용 -->
    <div class="my-page-container">
        <div class="page-title-container">
            <h1 class="page-title">My Page</h1>
        </div>
        <div class="tab">
            <div class="tab-item active" data-section="my-info" onclick="showSection('my-info')">내 정보</div>
            <div class="tab-item" data-section="my-posts" onclick="showSection('my-posts')">내 게시글</div>
            <div class="tab-item" data-section="send-messages" onclick="showSection('send-messages')">받은 쪽지 목록</div>
           	<div class="tab-item" data-section="rec-messages" onclick="showSection('rec-messages')">보낸 쪽지 목록</div>
        </div>
        <!-- My Information 섹션 -->
        <div class="section my-info active" id="my-info">
            <h2 class="title">내 정보</h2>
            <div class="input-field">
                <i class="fas fa-lock"></i>
                <label for="id">아이디: &nbsp; </label>
                <input type="id" id="id" value="<%= id %>" readonly>
            </div>
            <div class="input-field">
                <i class="fas fa-envelope"></i>
                <label for="email">이메일: &nbsp; </label>
                <input type="email" id="email" value="<%= email %>" readonly>
            </div>
            <div class="input-field">
                <i class="fas fa-user"></i>
                <label for="username">닉네임: &nbsp; </label>
                <input type="text" id="username" value="<%= username %>" readonly>
            </div>
            <button class="btn edit" onclick="showSection('edit-info')">수정하기</button> <!-- 수정하기 버튼 -->
        </div>
        <!-- Edit Information 섹션 -->
        <div class="section edit-info" id="edit-info" style="display: none;">
            <form action="/edit.do" method="POST">
                <h2 class="title">정보 수정</h2>
                <div class="input-field">
                    <i class="fas fa-lock"></i>
                    <label for="edit-password">비밀번호: &nbsp; </label>
                    <input type="password" id="edit-password" name="edit-password" value="<%= password %>">
                </div>
                <div class="input-field">
                    <i class="fas fa-envelope"></i>
                    <label for="edit-email">이메일: &nbsp; </label>
                    <input type="email" id="edit-email" name="edit-email" value="<%= email %>">
                </div>
                <div class="input-field">
                    <i class="fas fa-user"></i>
                    <label for="edit-username">닉네임: &nbsp; </label>
                    <input type="text" id="edit-username" name="edit-username" value="<%= username %>">
                </div>
                <button class="btn update" type="submit">수정 완료</button> <!-- 수정 완료 버튼 -->
            </form>
        </div>
        
        <!-- My Posts 섹션 -->
        <div class="section my-posts" id="my-posts">
		    <h2 class="title">내 게시글</h2>
		    <div class="post-list">
		        <div class="post-header">
		            <div class="post-title">제목</div>
		            <div class="post-pet-type">반려동물 종류</div>
		            <div class="post-date">작성 날짜</div>
		        </div>
		        <%
		            if (posts == null || posts.isEmpty()) {
		        %>
		            <div class="err">
		                <p class="err-msg">게시글이 없습니다.</p>
		            </div>
		        <%
		            } else {
		                for (AdoptDto post : posts) {
		        %>
		            <div class="post-item">
		                <div class="post-title"><a href="adopt?postId=<%= post.getId() %>"><%= post.getTitle() %></a></div>
		                <div class="post-pet-type"><%= post.getPetType() %></div>
		                <div class="post-date"><%= post.getCreatedAt() %></div>
		            </div>
		        <%
		                }
		            }
		        %>
		    </div>
		</div>

		<!-- send Message 섹션 -->
		<div class="section send-messages" id="send-messages">
		    <h2 class="title">받은 쪽지 목록</h2>
		    <div class="message-list">
		        <div class="message-header">
		            <div class="message-from">보낸 사람</div>
		            <div class="message-content">내용</div>
		            <div class="message-date">보낸 날짜</div>
		        </div>
		        <%
		            if (messages == null || messages.isEmpty()) {
		                System.out.println("No messages to display."); 
		        %>
		            <div class="err">
		                <p class="err-msg">받은 쪽지가 없습니다.</p>
		            </div>
		        <%
		            } else {
		                System.out.println("Messages size: " + messages.size());
		                for (MessageDto message : messages) {
		                    System.out.println("Message from: " + message.getFromUser() + " - " + message.getMessage());
		        %>
		            <div class="message-item">
		                <div class="message-from"><a href="message.jsp?toUser=<%= message.getFromUser() %>"><%= message.getFromUser() %></a></div>
		                <div class="message-content"><%= message.getMessage() %></div>
		                <div class="message-date"><%= message.getCreatedAt() %></div>
		            </div>
		        <%
		                }
		            }
		        %>
		    </div>
		</div>
		
		<!-- recieve Message 섹션 -->
		<div class="section rec-messages" id="rec-messages">
		    <h2 class="title">받은 쪽지 목록</h2>
		    <div class="message-list">
		        <div class="message-header">
		            <div class="message-from">보낸 사람</div>
		            <div class="message-content">내용</div>
		            <div class="message-date">보낸 날짜</div>
		        </div>
		        <%
		            if (messages == null || messages.isEmpty()) {
		                System.out.println("No messages to display."); 
		        %>
		            <div class="err">
		                <p class="err-msg">보낸 쪽지가 없습니다.</p>
		            </div>
		        <%
		            } else {
		                System.out.println("Messages size: " + messages.size());
		                for (MessageDto message : messages) {
		                    System.out.println("Message from: " + message.getFromUser() + " - " + message.getMessage());
		        %>
		            <div class="message-item">
		                <div class="message-from"><a href="message.jsp?toUser=<%= message.getFromUser() %>"><%= message.getFromUser() %></a></div>
		                <div class="message-content"><%= message.getMessage() %></div>
		                <div class="message-date"><%= message.getCreatedAt() %></div>
		            </div>
		        <%
		                }
		            }
		        %>
		    </div>
		</div>
    </div>
</div>

<script src="https://kit.fontawesome.com/a076d05399.js"></script>
<script>
    function showSection(sectionId) {
        // 선택한 섹션을 활성화하고 다른 섹션을 비활성화
        document.querySelectorAll('.section').forEach(section => {
            if (section.id === sectionId) {
                section.style.display = 'block';
            } else {
                section.style.display = 'none';
            }
        });

        // 선택한 탭을 활성화하고 다른 탭을 비활성화
        document.querySelectorAll('.tab-item').forEach(tabItem => {
            if (tabItem.getAttribute('data-section') === sectionId) {
                tabItem.classList.add('active');
            } else {
                tabItem.classList.remove('active');
            }
        });
    }
</script>
</body>
</html>
