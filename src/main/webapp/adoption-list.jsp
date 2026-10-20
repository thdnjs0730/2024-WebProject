<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.web.dao.AdoptDao" %>
<%@ page import="com.web.dto.AdoptDto" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>입양 게시글 목록</title>
    <link rel="stylesheet" href="assets/css/adoption-list.css">
    <style>
        .container{
            margin: 0 auto;
            padding: 20px;
        }

    </style>
</head>
<body>
    <%@include file="/WEB-INF/fragments/header.jspf"%>

    <%
        String searchQuery = request.getParameter("searchQuery");
        String searchType = request.getParameter("searchType");
        String petType = request.getParameter("petType");
        AdoptDao adoptDao = new AdoptDao();
        List<AdoptDto> posts = null;
        try {
            if ((searchQuery != null && !searchQuery.trim().isEmpty()) || (petType != null && !petType.isBlank())) {
                posts = adoptDao.searchPosts(searchQuery, searchType, petType);
            } else {
                posts = adoptDao.getAllPosts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    %>

    <!-- Page Content -->
    <div class="container">
        <div class="title-con">
            <h1 class="title">입양 게시글 목록</h1>
        </div>
        <div class="search-form-wrapper">
            <form method="get" action="adoption-list.jsp" class="search-form">
                <div class="checkbox-group">
                    <label for="dog">
                        <input type="radio" id="dog" name="petType" value="강아지" <%= "강아지".equals(petType) ? "checked" : "" %>>
                        <span>강아지</span>
                    </label>
                    <label for="cat">
                        <input type="radio" id="cat" name="petType" value="고양이" <%= "고양이".equals(petType) ? "checked" : "" %>>
                        <span>고양이</span>
                    </label>
                    <label for="other">
                        <input type="radio" id="other" name="petType" value="기타" <%= "기타".equals(petType) ? "checked" : "" %>>
                        <span>기타</span>
                    </label>
                </div>
                <select name="searchType">
                    <option value="title" <%= "title".equals(searchType) ? "selected" : "" %>>제목</option>
                    <option value="nickname" <%= "nickname".equals(searchType) ? "selected" : "" %>>작성자</option>
                </select>
                <input type="text" name="searchQuery" placeholder="검색어를 입력하세요..." value="<%= com.web.common.Html.escape(searchQuery) %>">
                <a href="adoption-list.jsp" class="button">리셋</a>
                <button type="submit" class="button">검색</button>
            </form>
            <a href="adoption-create.jsp" class="button">새 게시글 작성</a>
        </div>
        <div class="content">
            <%
                if (posts == null || posts.isEmpty()) {
            %>
                <div class="err">
                    <p class="err-ms">게시글이 없습니다.</p>
                </div>
            <%
                } else {
                    for (AdoptDto post : posts) {
            %>
                <div class="card">
                    <div class="card-con">
                        <img src="uploads/<%= com.web.common.Html.escape(post.getPhoto()) %>" class="card-img-top" alt="<%= com.web.common.Html.escape(post.getTitle()) %>" width="500px">
                        <div class="card-body">
                            <h5 class="card-title"><a href="adopt?postId=<%= com.web.common.Html.escape(post.getId()) %>"><%= com.web.common.Html.escape(post.getTitle()) %></a></h5>
                            <p class="card-text"><strong>작성일:</strong> <%= com.web.common.Html.escape(post.getCreatedAt()) %></p>
                            <p class="card-text"><strong>작성자 닉네임:</strong> <%= com.web.common.Html.escape(post.getNickname()) %></p>
                        </div>
                    </div>
                </div>
            <%
                    }
                }
            %>
        </div>
    </div>
</body>
</html>
