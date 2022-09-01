# 톰캣 구현하기

## 학습 목표
- HTTP와 서블릿에 대한 이해도를 높인다.
- 스레드, 스레드풀을 적용해보고 동시성 처리를 경험한다.

## 구현 요구사항

### 1단계 - HTTP 서버 구현하기
- [x] `http://localhost:8080/index.html`에 접속 가능하게 구현
  - [x] `Http11ProcessorTest` 통과
- [x] CSS 지원
- [x] Query String 파싱

### 2단계 - 로그인 구현하기
- [ ] 로그인 여부에 따른 페이지 이동
  - [ ] 성공 : 302, /index.html로 리다이렉트
  - [ ] 실패 : 401, /401.html
- [ ] POST 방식 회원가입
  - [ ] 회원 가입 완료 시 /index.html로 리다이렉트
  - [ ] 로그인 버튼도 POST로 수정
- [ ] Cookie - JSSESSIONID 저장 
  - [ ] HTTP Reqeust Header의 Cookie에 JSSESSIONID 값이 없으면 Response Header에 `Set-Cookie` 반환 기능 구현
- [ ] Session 구현 
