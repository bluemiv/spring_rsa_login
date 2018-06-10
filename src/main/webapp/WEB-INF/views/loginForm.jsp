<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>로그인</title>
<!-- javascript lib load -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script type="text/javascript" src="<c:url value="/resources/js/rsa/jsbn.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/rsa/rsa.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/rsa/prng4.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/rsa/rng.js" />"></script>
</head>
<body>
	<!-- 유저가 입력하는 form -->
	<form action="loginPro" method="post" id="loginForm">
		이메일:<input type="text" id="email" name="email" autofocus required /><br>
		비밀번호:<input type="password" id="password" name="password" required /><br>
		<button type="submit">로그인</button>
	</form>

	<!-- 실제 서버로 전송되는 form -->
	<form action="loginPro" method="post" id="hiddenForm">
		<input type="hidden" name="email" /> 
		<input type="hidden" name="password" />
	</form>
<script>
var resultMsg = "${resultMsg}";
if (resultMsg != "") {
	alert(resultMsg);
}
// Server로부터 받은 공개키 입력
var rsa = new RSAKey();
rsa.setPublic("${modulus}", "${exponent}");

var $email = $("#hiddenForm input[name='email']");
var $password = $("#hiddenForm input[name='password']");

$("#loginForm").submit(function(e) {
	// 실제 유저 입력 form은 event 취소
	// javascript가 작동되지 않는 환경에서는 유저 입력 form이 submit 됨
	// -> Server 측에서 검증되므로 로그인 불가
	e.preventDefault();

	var email = $(this).find("#email").val();
	var password = $(this).find("#password").val();

	$email.val(rsa.encrypt(email)); // 아이디 암호화
	$password.val(rsa.encrypt(password)); // 비밀번호 암호화

	$("#hiddenForm").submit();
});
</script>
</body>
</html>