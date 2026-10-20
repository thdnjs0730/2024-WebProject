<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*, java.util.*, com.web.common.JdbcConnectUtil,
    com.web.dao.MemberDao,com.web.dto.MemberDto,
    com.web.dao.AdoptDao, com.web.dto.AdoptDto" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
<!-- Bootstrap CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/adminPage.css">
<script type="text/javascript">
    function showAlert(message, redirect) {
        alert(message);
        if (redirect) {
            window.location = redirect;
        }
    }
</script>
</head>
<body>

<!-- 네비게이션 바 -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="adminPage.jsp">관리자 페이지</a>
  <div class="collapse navbar-collapse">
    <ul class="navbar-nav ml-auto">
      <li class="nav-item">
        <a class="btn btn-primary" href="index.jsp">홈페이지로</a>
      </li>
    </ul>
  </div>
</nav>

<div class="container">

    <!-- 탭 메뉴 -->
    <ul class="nav nav-tabs" id="myTab" role="tablist">
      <li class="nav-item">
        <a class="nav-link <%= (request.getAttribute("activeTab") == null || "member".equals(request.getAttribute("activeTab"))) ? "active" : "" %>" id="member-tab" data-toggle="tab" href="#member" role="tab" aria-controls="member" aria-selected="true">회원 관리</a>
      </li>
      <li class="nav-item">
        <a class="nav-link <%= "post".equals(request.getAttribute("activeTab")) ? "active" : "" %>" id="post-tab" data-toggle="tab" href="#post" role="tab" aria-controls="post" aria-selected="false">게시글 관리</a>
      </li>
    </ul>

    <!-- 탭 내용 -->
    <div class="tab-content" id="myTabContent">
        <!-- 회원 관리 탭 -->
        <div class="tab-pane fade <%= (request.getAttribute("activeTab") == null || "member".equals(request.getAttribute("activeTab"))) ? "show active" : "" %>" id="member" role="tabpanel" aria-labelledby="member-tab">
            <h2 class="text-center">회원 목록</h2>
            <!-- 검색 폼 추가 -->
            <form class="form-inline my-2 my-lg-0" method="get" action="adminPage.do?action=searchMembers">
                <input class="form-control mr-sm-2" type="search" placeholder="아이디 혹은 닉네임" name="keyword" aria-label="Search">
                <input type="hidden" name="action" value="searchMembers"> <!-- 검색 액션 추가 -->
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">회원 검색</button> &nbsp;
                <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="location.href='adminPage.jsp'">리셋</button>
            </form>
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Password</th>
                        <th>Email</th>
                        <th>Nickname</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
					    // 성공 또는 오류 메시지 출력
					    String successMessage = (String) request.getAttribute("successMessage");
					    String errorMessage = (String) request.getAttribute("errorMessage");
					    if (successMessage != null) {
					        out.println("<script type='text/javascript'>showAlert('" + successMessage + "', 'adminPage.jsp');</script>");
					    } else if (errorMessage != null) {
					        out.println("<script type='text/javascript'>showAlert('" + errorMessage + "', null);</script>");
					    }

					    String adminId = "adminid"; // 관리자 계정 ID
					    List<MemberDto> members = (List<MemberDto>) request.getAttribute("members");

					    if (members == null) {
					        members = new ArrayList<>();
					        String keyword = request.getParameter("keyword");
					        if (keyword != null && !keyword.trim().isEmpty()) {
					            MemberDao memberDao = new MemberDao();
					            members = memberDao.searchMembers(keyword);
					        } else {
					            try {
					                // 회원 정보 불러오기
					                MemberDao memberDao = new MemberDao();
					                members = memberDao.getAllMembers();
					            } catch (Exception e) {
					                e.printStackTrace();
					            }
					        }
					    }

					    for (MemberDto member : members) {
					        out.println("<tr>");
					        out.println("<form action='adminPage.do' method='post'>");
					        out.println("<td><input type='text' class='form-control' name='id' value='" + member.getId() + "' readonly></td>");
					        out.println("<td>" + member.getPassword() + "</td>");
					        out.println("<td>" + member.getEmail() + "</td>");
					        out.println("<td>" + member.getNickname() + "</td>");
					        if (member.getId().equals(adminId)) {
					            out.println("<td>관리자 계정</td>");
					        } else {
					            out.println("<td><button type='submit' name='action' value='DeleteMember' class='btn btn-danger btn-sm' onclick='return confirm(\"정말로 삭제하시겠습니까?\");'>회원 삭제</button></td>");
					        }
					        out.println("</form>");
					        out.println("</tr>");
					    }
					%>
                </tbody>
            </table>
        </div>
        <!-- 게시글 관리 탭 -->
        <div class="tab-pane fade <%= "post".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" id="post" role="tabpanel" aria-labelledby="post-tab">
            <h2 class="text-center">게시글 목록</h2>
            <!-- 검색 폼 추가 -->
            <form class="form-inline my-2 my-lg-0" method="get" action="adminPage.do?action=searchPosts">
                <input class="form-control mr-sm-2" type="search" placeholder="제목" name="keyword" aria-label="Search">
                <input type="hidden" name="action" value="searchPosts"> <!-- 검색 액션 추가 -->
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">게시글 검색</button> &nbsp;
                <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="location.href='adminPage.do?keyword=&action=searchPosts'">리셋</button>
            </form>
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Title</th>
			            <th>User ID</th>
			            <th>Created At</th>
			            <th>Action</th>
                    </tr>
                </thead>
                <tbody>
				    <%
				        // 성공 또는 오류 메시지 출력
				        successMessage = (String) request.getAttribute("successMessage");
				        errorMessage = (String) request.getAttribute("errorMessage");
				        if (successMessage != null) {
				            out.println("<script type='text/javascript'>showAlert('" + successMessage + "', 'adminPage.jsp');</script>");
				        } else if (errorMessage != null) {
				            out.println("<script type='text/javascript'>showAlert('" + errorMessage + "', null);</script>");
				        }

				        List<AdoptDto> posts = (List<AdoptDto>) request.getAttribute("posts");

				        if (posts == null) {
				            posts = new ArrayList<>();
				            String keyword = request.getParameter("keyword");
				            if (keyword != null && !keyword.trim().isEmpty()) {
				                AdoptDao adoptDao = new AdoptDao();
				                posts = adoptDao.searchPostsByTitle(keyword); // 검색어를 사용하여 제목으로 검색
				            } else {
				                try {
				                    AdoptDao adoptDao = new AdoptDao();
				                    posts = adoptDao.getAllPosts();
				                } catch (Exception e) {
				                    e.printStackTrace();
				                }
				            }
				        }

				        for (AdoptDto post : posts) {
				            out.println("<tr>");
				            out.println("<form action='adminPage.do' method='post'>");
				            out.println("<td><input type='text' class='form-control' name='title' value='" + post.getTitle() + "' readonly></td>");
				            out.println("<td>" + post.getUserId() + "</td>"); // user_id를 표시
				            out.println("<td>" + post.getCreatedAt() + "</td>");
				            out.println("<td><button type='submit' name='action' value='DeletePost' class='btn btn-danger btn-sm' onclick='return confirm(\"정말로 삭제하시겠습니까?\");'>게시글 삭제</button></td>");
				            out.println("<input type='hidden' name='id' value='" + post.getId() + "'>"); // id를 숨겨서 전송
				            out.println("</form>");
				            out.println("</tr>");
				        }
				    %>
				</tbody>

            </table>
        </div>
    </div>
</div>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
