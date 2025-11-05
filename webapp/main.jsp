<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PetCare | 메인</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<c:if test="${empty sessionScope.loginUser}">
  <script>
 	alert("로그인이 필요합니다.");
    location.href="<c:url value='/index.jsp'/>";
  </script>
</c:if>

<header>
  <h1>
    <a class="logo" href="<c:url value='/main.jsp'/>">
      <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
    </a>
  </h1>
  <nav>
    <a href="#" onclick="alert('커뮤니티는 준비 중입니다!')">커뮤니티</a>
    <a href="<c:url value='/owner/mypage.jsp'/>" class="mypage-link">
	  ${sessionScope.loginUser.nickname}??
	</a>
    <button class="logout-btn" onclick="location.href='<c:url value="/logout.do"/>'">log out</button>
  </nav>
</header>

<main>
  <h2>함께하는 일상이,<br>반려동물에게 더 큰 행복이 됩니다.</h2>
  <p>체중관리부터 커뮤니티 소통까지, 지금 바로 시작해보세요.</p>

  <div>
    <button class="main-btn" onclick="alert('케어 페이지는 준비 중입니다!')">케어정보 바로가기</button>
    <button class="community-btn" onclick="alert('커뮤니티는 준비 중입니다!')">커뮤니티 바로가기</button>
  </div>
</main>

<footer>© 2025 PetCare Mini Project | Team petcare</footer>
</body>
</html>
