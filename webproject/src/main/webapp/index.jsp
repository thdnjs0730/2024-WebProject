<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
    <link rel="stylesheet" href="css/index.css">
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
    <%@ include file="top.jsp" %>
    <!-- 이미지 슬라이더 섹션 -->
    <div class="carousel">
        <div class="carousel-inner">
            <div class="carousel-item">
                <img src="images/carousel-1.jpg" alt="Image 1">
            </div>
            <div class="carousel-item">
                <img src="images/carousel-2.jpg" alt="Image 2">
            </div>
            <div class="carousel-item">
                <img src="images/carousel-3.jpg" alt="Image 3">
            </div>
        </div>
        <button class="carousel-control prev" onclick="moveSlide(-1)">&#10094;</button>
        <button class="carousel-control next" onclick="moveSlide(1)">&#10095;</button>
    </div>
    
    <!-- 배너 -->
    <div class="banner">
        <img src="images/banner.gif">
    </div>
    
    <!-- 컨텐츠 섹션 -->
    <div class="main-content">
        <!-- 왼쪽 구역 -->
        <!-- 입양 -->
        <div class="adopt">
            <div class="title">
                <a href="adopt.jsp"><b>최근 입양 공고</b></a>
                <hr class="line">
            </div>
            <div class="adopt-content" style="overflow: hidden;">
                <iframe src="latestAdopt.do" style="border: none; width: 100%; height: 400px;"></iframe>
            </div>
        </div>
        <!-- 오른쪽 구역 -->
        <div class="content-right">
            <!-- 참여 -->
            <div class="board">
                <div class="title">
                    <a href="#"><b>커뮤니티</b></a>
                    <hr class="line">
                </div>
                <div class="board-content">
                    <a href="#">게시물 제목 1</a><br>
                    <a href="#">게시물 제목 2</a><br>
                    <a href="#">게시물 제목 3</a><br>
                    <a href="#">게시물 제목 4</a><br>
                    <a href="#">게시물 제목 5</a><br>
                </div>
            </div>
            <!-- 후원 -->
            <div class="don">
                <div class="don-img">
                    <img id="bannerImage" src="images/don1.png">
                </div>
                <div class="buttons">
                    <button data-image="images/don1.png">정기후원</button>
                    <button data-image="images/don2.png">일시후원</button>
                    <button data-image="images/don3.png">결연후원</button>
                </div>
            </div>
        </div>
    </div>
    <footer>
        <p>&copy; 2024 동양미래대학교 웹프로젝트 기말 과제</p>
    </footer>
    
    <script src="js/index.js"></script>
</body>
</html>
