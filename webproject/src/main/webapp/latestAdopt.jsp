<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최근 입양 게시글</title>

<style>
body{
	overflow: hidden; /* 스크롤바 숨김 */
}
#adopt-container {
    display: grid;
            grid-template-columns: repeat(3, 1fr); /* 3개의 열 */
            grid-template-rows: repeat(2, 4fr);    /* 2개의 행 */
            gap: 10px; /* 그리드 항목 간의 간격 */
            background-color: #fff;
            color: #444;
            height: 100vh; /* 전체 화면 높이 설정 */
}

.adopt-item {
    width: 100%;
    height: 90%;
    display: flex;
    flex-direction: column;
    border: 1px solid #ddd;
    overflow: hidden;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.adopt-image {
    position: relative;
    overflow: hidden;
    border-bottom: 1px solid #ddd;
}

.adopt-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease-in-out;
}

.adopt-image:hover img {
    transform: scale(1.05);
}

.adopt-text {
    position: relative;
    text-align: center;
    padding: 10px;
    background-color: rgba(255, 255, 255, 0.8);
    font-weight: bold;
    color: #333;
    z-index: 1;
}
</style>

</head>
<body>
    <div id="adopt-container">
        <c:forEach var="post" items="${posts}">
            <div class="adopt-item">
                <div class="adopt-image">
                    <a href="adoptDetail.do?postId=${post.id}" target="_top">
                        <img src="uploads/${post.photo}" alt="Adoptable pet ${post.title}">
                    </a>
                </div>
                <div class="adopt-text">${post.title}</div>
            </div>
        </c:forEach>
    </div>

</body>
</html>
