# 반려동물 입양 웹프로젝트

학교 팀 프로젝트의 JSP·Servlet·MySQL 소스를 실행 가능한 Maven WAR 프로젝트로 정리한 버전입니다. 입양 공고, 회원가입·로그인, 회원정보 수정, 받은/보낸 쪽지, 관리자 검색·삭제를 제공합니다. 커뮤니티 영역과 후원 배너는 화면 예시입니다.

## 빠른 실행: Docker

Docker Desktop의 Linux 컨테이너 환경에서 저장소 루트를 열고 실행합니다.

```powershell
Copy-Item .env.example .env
# .env의 DB_PASSWORD와 DB_ROOT_PASSWORD를 서로 다른 값으로 수정
docker compose up --build -d
```

브라우저에서 [http://localhost:8080](http://localhost:8080)을 엽니다. 최초 이미지 다운로드·빌드와 DB 초기화에는 시간이 걸립니다. 일반 회원은 화면에서 가입합니다. 관리자 계정이 필요하면 처음 실행하기 전에 `.env`의 `ADMIN_ID`와 `ADMIN_PASSWORD`를 지정합니다. 미설정 시 고정 관리자 계정을 만들지 않습니다.

```powershell
docker compose logs -f web
docker compose down
```

DB와 업로드 사진은 Docker 볼륨에 보존됩니다. `database/schema.sql`은 **새 DB를 만드는 용도**이며 기존 학교 DB에 적용하는 마이그레이션 파일이 아닙니다.

## Java·Tomcat으로 실행

Java 17, Maven 3.9, Tomcat 9, MySQL 8.4를 준비합니다. `javax.servlet` 프로젝트이므로 Tomcat 9를 사용합니다. [Tomcat 공식 문서](https://tomcat.apache.org/tomcat-9.0-doc/index.html)

1. MySQL에 `pet_adoption` 데이터베이스를 `utf8mb4`로 만들고 전용 사용자를 생성합니다.
2. 해당 DB에 `database/schema.sql`을 적용합니다. 예: MySQL 클라이언트에서 `USE pet_adoption;` 후 `SOURCE C:/경로/database/schema.sql;`.
3. `mvn verify`로 Java 컴파일·통합 테스트·WAR 생성을 실행합니다. 기본 테스트는 별도 H2 메모리 DB에서 수행하므로 MySQL이 없어도 빌드를 확인할 수 있습니다.
4. `target/pet-adoption.war`를 Tomcat의 `webapps`에 복사합니다.
5. Tomcat을 시작하는 프로세스에 아래 환경변수를 지정한 뒤 실행하고 [http://localhost:8080/pet-adoption/](http://localhost:8080/pet-adoption/)에 접속합니다.

```powershell
$env:DB_URL='jdbc:mysql://localhost:3306/pet_adoption?characterEncoding=UTF-8&serverTimezone=Asia/Seoul'
$env:DB_USER='pet_app'
$env:DB_PASSWORD='직접 지정한 DB 비밀번호'
$env:UPLOAD_DIR='C:\pet-adoption-data\uploads'
# 관리자를 처음 만들 때만 선택적으로 지정
$env:ADMIN_ID='your_admin'
$env:ADMIN_PASSWORD='직접 지정한 관리자 비밀번호'
& 'C:\apache-tomcat-9\bin\catalina.bat' run
```

`.env`는 Docker Compose가 읽습니다. Tomcat을 직접 실행할 때는 위처럼 프로세스 환경변수를 설정해야 합니다. ID는 영문·숫자·밑줄 3~32자, 비밀번호는 8자 이상이며 UTF-8 72바이트 이하입니다. 관리자 초기화는 동일 ID가 없을 때만 수행하며 기존 비밀번호를 덮어쓰지 않습니다.

## 폴더 구성

| 경로 | 역할 |
| --- | --- |
| `src/main/java/com/web/controller` | HTTP 요청 처리와 사진 응답 |
| `src/main/java/com/web/dao` | SQL 조회·저장 |
| `src/main/java/com/web/dto` | 회원·공고·쪽지 데이터 |
| `src/main/java/com/web/common` | DB 설정, 비밀번호 해시, HTML 이스케이프 |
| `src/main/java/com/web/mvc` | 인증·권한 필터와 초기 관리자 설정 |
| `src/main/webapp/*.jsp` | 역할별 화면: `auth`, `account`, `adoption-*`, `admin`, `about` |
| `src/main/webapp/WEB-INF/fragments` | 공통 헤더 |
| `src/main/webapp/assets` | `css`, `js`, `images` 정적 자료 |
| `database/schema.sql` | 새 DB 테이블과 제약조건 |
| `src/test/java` | 실제 Tomcat 기반 HTTP 통합 테스트 |
| `docs` | 변경·검증 기록과 기존 기획 자료 |

실행 시 생성된 사진은 소스 폴더 밖의 `UPLOAD_DIR`에 저장합니다. 미지정 시 사용자 홈의 `.pet-adoption/uploads`를 사용합니다. 최대 5MB의 PNG/JPEG/GIF를 이미지로 검증한 뒤 저장하며, 글 수정 시 사진을 다시 선택하지 않으면 기존 사진을 유지합니다.

## 검증

```powershell
mvn verify
# 실제 MySQL 검증은 비어 있는 전용 테스트 DB를 준비한 뒤 실행
$env:TEST_DB_URL='jdbc:mysql://localhost:3306/pet_adoption_test?characterEncoding=UTF-8&serverTimezone=Asia/Seoul'
$env:TEST_DB_USER='test_user'
$env:TEST_DB_PASSWORD='test_password'
mvn verify
```

테스트 DB에는 테스트 사용자·쪽지가 생성됩니다. `TEST_DB_URL`에 실제 사용자 DB를 지정하지 마세요. 매번 빈 전용 DB를 사용합니다. GitHub Actions는 MySQL 8.4와 `/pet-adoption`·루트 배포 경로를 검증합니다. 상세 결과는 [검증 기록](docs/verification.md)을 참고합니다.

## 원본과 정리 범위

`학교 자료/웹프로젝트/webproject`를 기준으로 화면과 이미지를 복구했습니다. 원본 학교 폴더는 수정하지 않았습니다. 중복된 초기 JSP와 Eclipse 빌드 산출물은 현재 실행 소스에서 제외했으며 기존 버전은 Git 이력에 남아 있습니다. 기획 자료는 `docs/project-history`에 모았습니다.

DB 비밀번호의 소스 내 고정을 제거하고 회원 비밀번호를 BCrypt로 저장합니다. 기존 평문 비밀번호 DB를 그대로 연결하는 방식은 지원하지 않습니다. 이 버전은 새 DB로 실행합니다. 학습용 화면과 기존 디자인을 유지했으며 운영 환경의 메일 인증·비밀번호 복구·요청 제한·HTTPS 배포는 포함하지 않습니다.
