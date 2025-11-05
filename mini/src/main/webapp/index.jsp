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
    <a class="logo" href="<c:url value='/index.jsp'/>">
      <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
    </a>
  </h1>
  <nav>
    <button class="login-btn" onclick="location.href='<c:url value="/owner/login.jsp"/>'">log in</button>
  </nav>
</header>

<main>
  <h2>함께하는 일상이,<br>반려동물에게 더 큰 행복이 됩니다.</h2>
  <p>체중관리부터 커뮤니티 소통까지, 지금 바로 시작해보세요.</p>

  <button class="main-btn" onclick="location.href='<c:url value="/owner/join.jsp"/>'">
    회원가입 바로가기
  </button>
</main>

<footer>© 2025 PetCare Mini Project | Team petcare</footer>
</body>
</html>
