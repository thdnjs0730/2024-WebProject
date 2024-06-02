<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>	
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="css/home.css?after">
</head>
<body>
    <div class="box"></div>
    <header>
        <div class="logo"><a href="home.jsp"><img src="images/logo.png" width="80px"></a></div>
        <nav class="nav-left">
            <ul>
                <li><a href="#" class="underline">홈</a></li>
                <li><a href="#" class="underline">입양하기</a></li>
                <li><a href="#" class="underline">참여하기</a></li>
                <li><a href="#" class="underline">후원하기</a></li>
            </ul>
        </nav>
        <nav class="nav-right">
            <ul>
                <li><a href="#" class="underline">로그인</a></li>
                <li><a href="#" class="underline">회원가입</a></li>
                <li><a href="#" class="underline">마이페이지</a></li>
            </ul>
        </nav>
    </header>
    
    <div class="carousel">
        <div class="carousel-text">
            <p class="bold"><b>반려동물이&nbsp;행복한 세상</b></p>
            <p class="normal">반려동물과 반려인이 행복해지는 그날까지</p>
        </div>
        <div class="carousel-inner">
            <img src="images/carousel1.jpg" class="carousel-item" alt="Image 1">
            <img src="images/carousel2.jpg" class="carousel-item" alt="Image 2">
            <img src="images/carousel3.jpg" class="carousel-item" alt="Image 3">
        </div>
        <button class="carousel-control prev" onclick="moveSlide(-1)">&#10094;</button>
        <button class="carousel-control next" onclick="moveSlide(1)">&#10095;</button>
    </div>

    <div class="main-content">
        <div class="pre-adopt">
            <div class="pre-adopt-title">
                <a href="#" class="pre"><b>최근 입양 공고</b></a>
                <hr class="orangeline">
            </div>
            <div class="image-container-wrapper">
                <div class="image-container">
                    <a href="#"><img src="images/preadopt1.png"></a>
                    <div class="overlay-text">입양 공고 1</div>
                </div>
                <div class="image-container">
                    <a href="#"><img src="images/preadopt2.png"></a>
                    <div class="overlay-text">입양 공고 2</div>
                </div>
                <div class="image-container">
                    <a href="#"><img src="images/preadopt3.png"></a>
                    <div class="overlay-text">입양 공고 3</div>
                </div>
                <div class="image-container">
                    <a href="#"><img src="images/preadopt4.png"></a>
                    <div class="overlay-text">입양 공고 4</div>
                </div>
                <div class="image-container">
                    <a href="#"><img src="images/preadopt5.png"></a>
                    <div class="overlay-text">입양 공고 5</div>
                </div>
                <div class="image-container">
                    <a href="#"><img src="images/preadopt6.png"></a>
                    <div class="overlay-text">입양 공고 6</div>
                </div>
            </div>
        </div>
        
        <div class="pre-board">
            <div class="pre-board-title">
                <a href="#" class="pre"><b>커뮤니티</b></a>
                <hr class="orangeline">
            </div>
            <div class="pre-board-content">
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 1</a></div>
                    <div class="post-body">게시물 내용 1...</div>
                </div>
                <hr>
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 2</a></div>
                    <div class="post-body">게시물 내용 2...</div>
                </div>
                <hr>
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 3</a></div>
                    <div class="post-body">게시물 내용 3...</div>
                </div>
                <hr>
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 4</a></div>
                    <div class="post-body">게시물 내용 4...</div>
                </div>
                <hr>
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 5</a></div>
                    <div class="post-body">게시물 내용 5...</div>
                </div>
                <hr>
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 6</a></div>
                    <div class="post-body">게시물 내용 6...</div>
                </div>
                <hr>
                <div class="post">
                    <div class="post-title"><a href="#">게시물 제목 7</a></div>
                    <div class="post-body">게시물 내용 7...</div>
                </div>
            </div>
        </div>
    </div>
    <div class="pre-dona">
        <div class="pre-dona-title">
            <a href="#" class="pre"><b>후원하기</b></a>
            <hr class="orangeline">
        </div>
        <div class="pre-dona-box">
            <p class="dona-bold"><a href="#">정기후원</a></p>
            <p class="dona-normal">동물 구조, 학대대응, 캠페인, 교육 등 단체 모든 활동을 후원합니다.</p>
        </div>
        <div class="pre-dona-box">
            <p class="dona-bold"><a href="#">일시후원</a></p>
            <p class="dona-normal">신용카드, 핸드폰, 계좌이체를 통해 후원금을 납부하는 일시 후원 방법입니다.</p>
        </div>
        <div class="pre-dona-box">
            <p class="dona-bold"><a href="#">결연후원</a></p>
            <p class="dona-normal">온센터 동물들의 보호와 치료,입양을 후원합니다.</p>
        </div>
    </div>
    <div class="box"></div>
    <footer>
        <div class="footer-content">
            <p>&copy; 2024 동양미래대학교 웹프로젝트 기말 과제</p>
        </div>
    </footer>
    <script src="js/homescript.js"></script>
</body>
</html>
