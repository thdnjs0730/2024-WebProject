let slideIndex = 0;
let slides = document.getElementsByClassName("carousel-item");
let slideInterval = setInterval(showNextSlide, 4000); // 자동 슬라이드 설정 (4초마다)

function moveSlide(n) {
    clearInterval(slideInterval); // 수동 전환 시 자동 슬라이드 멈춤
    showSlides(slideIndex += n);
    slideInterval = setInterval(showNextSlide, 4000); // 자동 슬라이드 재설정
}

function showSlides(n) {
    if (n >= slides.length) { slideIndex = 0 }
    if (n < 0) { slideIndex = slides.length - 1 }
    let offset = -slideIndex * 100;
    let carouselInner = document.querySelector(".carousel-inner");
    carouselInner.style.transform = `translateX(${offset}%)`;
}

function showNextSlide() {
    moveSlide(1);
}

showSlides(slideIndex);

// 후원 이미지 변경 함수
function changeImage(imagePath) {
    document.getElementById('bannerImage').src = imagePath;
}

// 후원 버튼 클릭 이벤트 설정
document.querySelectorAll('.buttons button').forEach(button => {
    button.addEventListener('click', (event) => {
        const imagePath = event.target.getAttribute('data-image');
        changeImage(imagePath);
    });
});
