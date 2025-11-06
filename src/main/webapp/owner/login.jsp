<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PetCare | 로그인</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<header>
  <h1><a class="logo" href="<c:url value='/index.do'/>"><img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고"></a></h1>
  <nav>
    <a href="<c:url value='/join.do'/>">회원가입</a>
    <button class="login-btn" onclick="location.href='<c:url value="/login.do"/>'">log in</button>
  </nav>
</header>

<div class="form-container">
  <h3>로그인</h3>
  <form action="<c:url value='/login.do'/>" method="post" autocomplete="off">
    <div class="field"><input type="text" name="userid" placeholder="아이디" required></div>
    <div class="field"><input type="password" name="pwd" placeholder="비밀번호" required></div>
    <button type="submit" class="submit-btn">로그인</button>
  </form>
</div>
</body>
</html>
