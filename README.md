# spring_rsa_login
Spring Framework와 RSA를 이용한 로그인
Spring Framework version : 3.1.1RELEASE
java 1.6

RSA 암호화를 위한 js파일 다운로드 : http://www-cs-students.stanford.edu/~tjw/jsbn/

주의할 점:jsbn.js, rsa.js, prng4.js, rng.js 순으로 include 해야 함.

로직 실행 순서
1. 클라이언트로부터 로그인 페이지 요청이 들어온다.
2. 서버는 개인키는 세션에 저장하고 공개키를 클라이언트 뷰로 넘긴다.
3. 클라이언트는 아이디/비밀번호를 입력하고 로그인 시도를 하면, 서버한테 받은 공개키를 가지고 js를 이용해 암호화를 한다. 암호화된 정보를 서버에게 전송한다.
4. 서버는 세션에 있는 개인키를 먼저 확인한다. 개인키가 없다면 부적절한 경로로 접근을 한 경우이므로 접근 할 수 없게 한다. 반면 세션에 개인키가 있다면 암호화된 클라이언트 정보를 복호화 하여 로그인 로직을 수행한다.
