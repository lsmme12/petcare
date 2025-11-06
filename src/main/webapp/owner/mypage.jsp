<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지 | PetCare</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
<style>
.mypage-container { width: 900px; margin: 80px auto; background-color: #fff9e8; border-radius: 25px; padding: 40px 50px; box-shadow: 0 3px 12px rgba(0,0,0,0.1); text-align: left; }
.mypage-header { display: flex; justify-content: space-between; align-items: center; }
.mypage-info { display: flex; align-items: flex-start; gap: 40px; margin-top: 30px; }
.profile-photo { width: 130px; height: 130px; background-color: #e6d8c3; border-radius: 50%; flex-shrink: 0; }
.user-info p { margin: 6px 0; font-size: 15px; color: #3b2b1c; }
.mypage-section { margin-top: 40px; display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.section-box { height: 120px; background-color: #fff; border-radius: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.update-btn { background-color: #4d2b13; color: #fff; border: none; border-radius: 20px; padding: 8px 20px; cursor: pointer; font-weight: 600; }
.update-btn:hover { background-color: #663818; }
</style>
</head>
<body>
<c:if test="${empty sessionScope.loginUser}">
  <script>
    alert("로그인이 필요합니다.");
    location.href = '<c:url value="/login.do"/>';
  </script>
</c:if>

<header>
  <h1>
    <a class="logo" href="<c:url value='/main.do'/>">
      <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
    </a>
  </h1>
  <nav>
    <a href="#" onclick="alert('커뮤니티는 준비중입니다!')">커뮤니티</a>
    <span>${sessionScope.loginUser.nickname} 님</span>
    <button class="logout-btn" onclick="location.href='<c:url value="/logout.do"/>'">log out</button>
  </nav>
</header>

<div class="mypage-container">
  <div class="mypage-header">
    <h2>마이페이지</h2>
    <button class="update-btn" onclick="location.href='<c:url value="/owner/update.do"/>'">수정</button>
  </div>

  <div class="mypage-info">
    <div class="profile-photo"></div>
    <div class="user-info">
      <p><strong>이름 :</strong> ${sessionScope.loginUser.name}</p>
      <p><strong>아이디 :</strong> ${sessionScope.loginUser.userid}</p>
      <p><strong>닉네임 :</strong> ${sessionScope.loginUser.nickname}</p>
      <p><strong>이메일 :</strong> ${sessionScope.loginUser.email}</p>
    </div>
  </div>

  <div class="mypage-section">
    <div class="section-box"></div>
    <div class="section-box"></div>
    <div class="section-box"></div>
  </div>
</div>

<footer>© 2025 PetCare Mini Project | Team petcare</footer>
</body>
</html>
