## 🦁 멋쟁이 사자처럼 백엔드 스쿨 2기 팀 프로젝트 🦁


### ✨ 프로젝트 주제 및 목표
- 멋쟁이 사자처럼 백엔드 스쿨 2기에서 배웠던 내용을 토대로 팀 프로젝트를 진행한다.
- SpringBoot, JPA, JWT, API를 사용하여 '버킷리스트 멤버를 모집'하는 서비스를 만든다.
- 도전과제 : 페이징 언어, 채팅 번역, 개인정보, 페이팔 결제 api

### 🙋‍♀️ 멤버 및 역할
- 최수정(PM)
- 정재성(CTO)
- 고관운
- 박은빈
- 배지원
- 변지환

### 📅 일정
- 2023년 1월 13일 : 아이디어 회의 및 발표
- 2023년 1월 17일, 18일 : 서류 작성
- 2023년 1월 19일 ~ 2월 : ??
- 2023년 2월 17일 : 데모데이

### 🎯 프로젝트 과정(완성되면 편집 불가로 설정하여 url 공유 예정)

- [📄 노션 프로젝트 페이지](https://ringed-suggestion-46f.notion.site/56e1241b82724ce18640168d351920bb)
- [🎞 스토리 보드](url 복붙)
- [🕹 기능 정의서](url 복붙)
- [🖥 서비스 UI](url 복붙)

### 🔖 프로젝트 요구사항 분석 및 체크리스트

#### 프로젝트 기술스택
- 에디터 : Intellij Ultimate
- 개발 툴 : SpringBoot 3.0.1
- 자바 : JAVA 17
- 빌드 : Gradle 6.8
- 서버 : AWS EC2
- 데이터베이스 : MySql 8.0
- 라이브러리 : Lombok, Spring Configuration Processor, Spring Web, Spring Data JPA, MySQL Driver, Thymeleaf

#### 기능명세서

![api1](/uploads/1d99611370959d2fc529d22cf005d218/api1.jpg)
![api2](/uploads/853c95e256d8adf587904e8235aad901/api2.jpg)

#### ERD ([erdcloud](https://www.erdcloud.com/d/Y9ZeR96ohN2erCebh))

![버킷리스트__2_](/uploads/1a6e5ed871b003186364e12243f615b9/버킷리스트__2_.png)

#### ERROR CODE(미완)
| 에러 코드 | 설명                                                                           | HTTP status |
| --- |------------------------------------------------------------------------------|-------------|
| DUPLICATED_USER_NAME | 중복된 아이디로 가입을 요청한 경우                                                          | 409         |
| USERNAME_NOT_FOUND | 가입되지 않은 아이디로 요청한 경우                                                          | 404         |
| INVALID_PASSWORD | 비밀번호가 틀린 경우                                                                  | 401         |
| INVALID_TOKEN | 유효하지 않은 토큰으로 요청한 경우                                                          | 401         |
| INVALID_PERMISSION | 해당 API에 대한 요청 권한이 없는 경우,<br> 올바르지 않은 헤더로 요청한 경우,<br> 게시글에 대한 수정/삭제 권한이 없는 경우 | 401         |
| POST_NOT_FOUND | 존재하지 않는 게시물을 요청한 경우                                                          | 404         |
| COMMENT_NOT_FOUND | 존재하지 않는 댓글을 요청한 경우                                                           | 404         |
| DATABASE_ERROR | DB와의 연결 이상인 경우                                                               | 500         |
| INTERNAL_SERVER_ERROR | 서버에서 요청 처리 중 오류가 발생한 경우                                                      | 500         |
| INVALID_VALUE | 지원하지 않는 포맷 입력                                                                | 400         |
| INVALID_REQUEST | 올바르지 않은 방법으로 요청한 경우, <br>좋아요를 2회이상 요청한 경우 | 409         |

#### 기능 개발
회원가입
- [ ] 회원가입 페이지 이동
- [ ] 아이디, 비밀번호, 이메일, 생년월일(8자리) 입력
- [ ] 이메일 인증
- [ ] 회원가입 완료

로그인
- [ ] 로그인 페이지 이동
- [ ] 로그인 완료

로그아웃
- [ ] 로그아웃 완료

프로필
- [ ] 해당 회원의 프로필 페이지 이동
- [ ] 작성한 게시글 띄우기
- [ ] 프로필 수정(뭘 수정할지는 미정)
- [ ] delete... 할거야?

유저평가(미완)
- [ ] 특정 유저의 평가 리스트
- [ ] 특정 유저의 평가 받기 받기
- [ ] 특정 유저에 대한 평가
- [ ] 특정 유저에 대한 평가 작성

멤버십
- [ ] 멤버십 조회
- [ ] 멤버십 결제(api)
- [ ] 멤버십 변경

채팅(미완)
- [ ] 채팅 목록 조회
- [ ] 버킷리스트가 시작되면 채팅방 생성
- [ ] 버킷리스트가 종료되면 채팅방 삭제

게시글
- [ ] 게시글 리스트 출력
- [ ] 특정 게시글 출력
- [ ] 게시글 작성
- [ ] 본인 게시글 수정
- [ ] 본인 게시글 삭제
- [ ] 게시글 리뷰 리스트 출력
- [ ] 게시글 리뷰 작성

댓글
- [ ] 댓글 출력
- [ ] 댓글 작성
- [ ] 댓글 수정
- [ ] 댓글 삭제

신청서 작성
- [ ] 해당 게시물에 대한 신청서 리스트 출력
- [ ] 해당 게시물에 신청서 작성

좋아요
- [ ] 좋아요 누르기
- [ ] 좋아요 취소
- [ ] 좋아요 개수

검색
- [ ] 검색을 통해 해당 단어를 가진 게시물 출력

#### 🖥 UI 개발
[템플릿](https://themes.iamabdus.com/star/2.2/index.html)


