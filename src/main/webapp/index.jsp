<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PetCare | 홈</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<header>
  <h1>
    <a class="logo" href="<c:url value='/index.do'/>">
      <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
    </a>
  </h1>
  <nav>
    <button class="login-btn" onclick="location.href='<c:url value="/login.do"/>'">log in</button>
  </nav>
</header>

<main>
  <h2>반려인의 일상,<br>PetCare와 함께하세요.</h2>
  <p>운동 관리와 커뮤니티, 투약 기록을 한 번에 관리해 보세요.</p>

  <button class="main-btn" onclick="location.href='<c:url value="/join.do"/>'">회원가입 바로가기</button>
</main>

<footer>© 2025 PetCare Mini Project | Team petcare</footer>
</body>
</html>
