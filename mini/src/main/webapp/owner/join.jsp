<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PetCare | 회원가입</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
<script>
function openIdCheck(){
  const v = document.getElementById('userid').value.trim();
  if(!v){ alert('아이디를 입력하세요.'); return; }
  window.open('<c:url value="/idCheck.do"/>?userid='+encodeURIComponent(v),
              'idcheck','width=420,height=300,top=150,left=300');
}
</script>
</head>
<body>
<header>
  <h1><a class="logo" href="<c:url value='/index.jsp'/>"><img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고"></a></h1>
  <nav>
    <button class="login-btn" onclick="location.href='<c:url value="/owner/login.jsp"/>'">log in</button>
  </nav>
</header>

<div class="form-container">
  <h3>회원가입</h3>
  <form action="<c:url value='/join.do'/>" method="post" autocomplete="off">
    <div class="field"><input type="text" name="name" placeholder="성명" required></div>

    <div class="field field-inline">
      <input type="text" id="userid" name="userid" placeholder="아이디" required>
      <button type="button" class="check-btn" onclick="openIdCheck()">중복체크</button>
    </div>

    <div class="field">
      <input type="password" name="pwd" placeholder="비밀번호" required>
      <div class="hint">비밀번호는 8~16자를 입력해주세요</div>
    </div>

    <div class="field">
      <input type="password" name="pwd_check" placeholder="비밀번호 확인" required>
    </div>

    <div class="field">
      <input type="text" name="nickname" placeholder="닉네임" required>
    </div>

    <div class="field">
      <input type="email" name="email" placeholder="이메일" required>
    </div>

    <button type="submit" class="submit-btn">가입</button>
  </form>
</div>

<footer>© 2025 PetCare Mini Project | Team 4</footer>
</body>
</html>
