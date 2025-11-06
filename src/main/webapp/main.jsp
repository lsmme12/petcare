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
    location.href = '<c:url value="/index.do"/>';
  </script>
</c:if>

<header>
  <h1>
    <a class="logo" href="<c:url value='/main.do'/>">
      <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
    </a>
  </h1>
  <nav>
    <a href="<c:url value='/post/list.do'/>">커뮤니티</a>
    <a href="<c:url value='/care/main.do'/>">펫케어</a>
    <a href="<c:url value='/owner/mypage.do'/>" class="mypage-link">
      <c:choose>
        <c:when test="${not empty sessionScope.loginUser}">
          ${sessionScope.loginUser.nickname} 님
        </c:when>
        <c:otherwise>
          로그인하기
        </c:otherwise>
      </c:choose>
    </a>
    <button class="logout-btn" onclick="location.href='<c:url value="/logout.do"/>'">log out</button>
  </nav>
</header>

<main>
  <h2>반려인의 일상,<br>PetCare와 함께하세요</h2>
  <p>커뮤니티 소통과 운동/투약 관리까지 한곳에서 편하게 이용해 보세요.</p>

  <div>
    <button class="community-btn" onclick="location.href='<c:url value="/post/list.do"/>'">커뮤니티 바로가기</button>
    <button class="care-btn" onclick="location.href='<c:url value="/care/main.do"/>'">펫케어 바로가기</button>
  </div>
</main>

<footer>© 2025 PetCare Mini Project | Team petcare</footer>
</body>
</html>


