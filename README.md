# 반려동물 입양 웹사이트

반려동물 입양 공고와 회원·메시지 기능을 다루는 학교 팀 프로젝트입니다.

## 역할

최소원은 Java Servlet·JSP·MySQL 기반 백엔드와 DB 연동을 담당했습니다. 사용자 입력의 저장·조회 흐름을 구현한 경험을 설명하는 자료이며 금융 거래나 상용 서비스 운영 경력으로 기재하지 않습니다.

## 복원한 코드

2026-10-20 사용자 보관 학교 ZIP에서 Java 16개와 JSP 13개를 비롯한 텍스트 소스를 webproject/ 아래에 복원했습니다. DB 접속값과 고정 관리자 비밀번호는 게시 전에 환경변수로 분리했습니다. 컴파일 산출물·라이브러리 JAR·사용자 데이터는 추가하지 않았습니다.

| 위치 | 내용 |
| --- | --- |
| [controller](webproject/src/main/java/com/web/controller) | 회원·입양 공고·메시지·마이페이지 요청 처리 |
| [dao](webproject/src/main/java/com/web/dao) | PreparedStatement 기반 DB 접근 |
| [dto](webproject/src/main/java/com/web/dto) | 요청과 DB 사이의 데이터 모델 |
| [JdbcConnectUtil](webproject/src/main/java/com/web/common/JdbcConnectUtil.java) | 환경변수를 읽는 JDBC 연결 |
| [JSP와 화면 자료](webproject/src/main/webapp) | 서버 렌더링 화면·CSS·JavaScript |
| [schema.sql](webproject/database/schema.sql) | 원본 테이블 생성문. 실제 사용자 행 없음 |

화면 입력 → Servlet → DAO → MySQL → DTO·JSP 순서로 흐름을 살펴볼 수 있습니다. 루트의 기존 JSP·CSS·이미지·계획서는 초기 작업 자료로 보존했으며, 복원한 백엔드 기준은 webproject/입니다.

## 실행 준비

원본 Eclipse 설정은 Java 17 · Tomcat 9(Servlet 4.0) 기준입니다. Java 17·Maven·Tomcat 9·MySQL 8 환경을 준비합니다.

1. [schema.sql](webproject/database/schema.sql)을 별도 로컬 DB에 적용합니다. 이 파일은 DDL이며 실제 사용자 데이터는 없습니다.
2. Tomcat 프로세스에 DB_URL, DB_USER, DB_PASSWORD 환경변수를 설정합니다. 예시는 [환경변수 안내](webproject/.env.example)를 참고합니다. .env 파일을 자동으로 읽지는 않습니다.
3. webproject/에서 mvn package를 실행하고 생성 WAR를 Tomcat에 배포합니다. Maven 설정은 이번 복원에서 추가했으며 실제 빌드 검증 여부는 [복원 기록](docs/restoration.md)을 따릅니다.
4. 관리자가 필요한 로컬 실습에만 BOOTSTRAP_ADMIN_ID와 BOOTSTRAP_ADMIN_PASSWORD를 명시합니다. 미설정 시 관리자 자동 생성은 하지 않습니다.

## 한계와 후속 작업

학교 원본에는 평문 비밀번호 비교와 짧은 비밀번호 컬럼 등 개선할 부분이 있습니다. 해시 저장·입력 검증·접근 권한·출력 이스케이프 등을 보완하기 전 운영 서비스로 사용하지 않습니다. 이번 작업은 포트폴리오용 소스 복원과 접속값 분리이며 인증 체계를 완성한 작업이 아닙니다.

ZIP의 바이너리 이미지 전체를 새로 게시하지 않았습니다. 일부 JSP가 참조하는 이미지·SVG는 누락될 수 있습니다. 전체 화면과 DB 연동의 재현은 별도 확인이 필요합니다.
