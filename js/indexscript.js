/* 기존 코드 */
const wrap = document.getElementsByClassName('wrap')[0];
const section = document.getElementsByClassName('section');
let page = 0;
const lastPage = section.length - 1;

window.addEventListener('wheel', (event) => {
  	event.preventDefault(); // 새로고침 막기
  	// deltaY -> 상하 스크롤량 반환 (읽기 전용 속성, 최신브라우저 지원 사파리X)
  	if(event.deltaY > 0){
    	page++;
  	}
  	if(event.deltaY < 0){
    	page--;
  	}
  	if(page < 0){
    	page = 0;
  	}
  	if(page > lastPage){
    	page = lastPage;
  	}
  	console.log(event.deltaY);
  	wrap.style.top = page * -100 + 'vh';
  	// wrap 클래스에 있는 css top 속성값을 변경시키는 코드
  	// 현재 보여지는 페이지에 따라서 +- 100vh 를 이동시킨다.
}, {passive: false}); // passive 디폴트 기능 제거 - 스크롤

document.addEventListener('DOMContentLoaded', () => {
    const overlay = document.querySelector('.overlay');
    
    // 마우스 이동 이벤트 핸들러
    document.addEventListener('mousemove', (e) => {
        const mouseX = e.clientX;
        const mouseY = e.clientY;
        updateOverlayMask(mouseX, mouseY);
    });

    // 스크롤 이벤트 핸들러
    window.addEventListener('scroll', () => {
        const mouseX = window.innerWidth / 2; // 화면의 중앙을 기준으로
        const mouseY = window.innerHeight / 2; // 화면의 중앙을 기준으로
        updateOverlayMask(mouseX, mouseY);
    });

    function updateOverlayMask(mouseX, mouseY) {
        overlay.style.setProperty('--mouse-x', `${mouseX}px`);
        overlay.style.setProperty('--mouse-y', `${mouseY}px`);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const menuButton = document.getElementById('menu-button');
    const menu = document.getElementById('menu');

    // 초기 상태로 메뉴 숨기기
    menu.style.display = 'none';

    menuButton.addEventListener('mouseover', () => {
        menu.style.display = 'block';
    });

    menuButton.addEventListener('mouseout', () => {
        menu.style.display = 'none';
    });
});

