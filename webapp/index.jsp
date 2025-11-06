<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>PetCare | 홈</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
  <style>
    .tiles { display:flex; gap:16px; margin-top:18px; }
    .tile { padding:16px 18px; border:1px solid #e5e7eb; border-radius:12px; }
    .tile h3 { margin:0 0 8px; }
    .actions { margin-top:16px; display:flex; gap:10px; }
  </style>
</head>
<body>
  <header>
    <h1>
      <a class="logo" href="<c:url value='/index.jsp'/>">
        <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
      </a>
    </h1>
    <nav>
      <c:choose>
        <c:when test="${empty sessionScope.loginUser}">
          <button class="login-btn" onclick="location.href='/owner/login.jsp'">log in</button>
          <a href="<c:url value='/owner/join.jsp'/>" class="mypage-link">회원가입</a>
        </c:when>
        <c:otherwise>
          <a href="<c:url value='/post/list.do'/>">커뮤니티</a>
          <a href="<c:url value='/modules/Petcare/index.jsp'/>">펫케어</a>
          <a href="<c:url value='/owner/mypage.jsp'/>" class="mypage-link">${sessionScope.loginUser.nickname}님</a>
          <button class="logout-btn" onclick="location.href='/logout.do'">log out</button>
        </c:otherwise>
      </c:choose>
    </nav>
  </header>

  <main>
    <h2>함께하는 일상, 반려동물에게 행복을</h2>
    <p>체중관리부터 커뮤니티 소통까지, 지금 바로 시작해보세요.</p>

    <c:choose>
      <c:when test="${empty sessionScope.loginUser}">
        <div class="actions">
          <button class="main-btn" onclick="location.href='<c:url value="/owner/login.jsp"/>'">로그인하러 가기</button>
          <button class="community-btn" onclick="location.href='<c:url value="/owner/join.jsp"/>'">회원가입</button>
        </div>
      </c:when>
      <c:otherwise>
        <div class="tiles">
          <div class="tile">
            <h3>커뮤니티</h3>
            <p>반려인들과 소통하고 정보를 나눠요.</p>
            <button onclick="location.href='/post/list.do'">바로가기</button>
          </div>
          <div class="tile">
            <h3>펫케어</h3>
            <p>진료 기록과 비만 체크 등 건강관리.</p>
            <button onclick="location.href='/modules/Petcare/index.jsp'">바로가기</button>
          </div>
        </div>
      </c:otherwise>
    </c:choose>
  </main>

  <footer>© 2025 PetCare Mini Project | Team petcare</footer>
</body>
</html>