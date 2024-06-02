<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
    
    <div id="circle"></div>
    
    <header>
        <p>Contact us&nbsp;<b>동양미래대학교 인공지능소프트웨어학과</b></p>
        <a href="home.jsp"><img src="images/logo.png" height="100px"></a>
        <hr>
        <div class="vr"></div>
    </header>  
    <div class="wrap">
        <div class="section" id="section1" style="background-image: url('images/main1.jpg')">
            <div class="overlay"></div>
            <h1>Adopt Animals<br>Adopt Family</h1>
            <h5>사지 말고 입양하세요.</h5>
        </div>
        <div class="section" id="section2" >
            <div class="overlay"></div>
            <div><h1>입양하기</h1></div>
            <button id="adopt"><a href="#"><b>입양하러 가기</b></a></button>
            <div class="img">
                <img src="images/sec2_img1.png">
                <img src="images/sec2_img2.png">
                <img src="images/main1.jpg">
            </div>
        </div>
    </div>      
    <footer>
        <hr>
        <p><b>&copy; 2024 웹프로젝트 기말 과제</b></p>
    </footer>
    <div id="follower"></div>
    <script src="js/indexscript.js"></script>
    
</body>
</html>
