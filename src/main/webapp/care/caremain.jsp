<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PetCare | 케어 허브</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
<style>
</style>
</head>
<body>
<header>
  <h1>
    <a class="logo" href="<c:url value='/main.do'/>">
      <img src="<c:url value='/img/logo.png'/>" alt="PetCare 로고">
    </a>
  </h1>
  <nav>
    <a href="<c:url value='/post/list.do'/>">커뮤니티</a>
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
<c:choose>
    <c:when test="${pet != null}">
        <div style="border:1px solid #ccc; padding:10px; width:300px;">
            <p><b>이름:</b> ${pet.petName}</p>
            <p><b>성별:</b> ${pet.sex}</p>
            <p><b>생년월일:</b> ${pet.birthDate}</p>
            <p><b>체중(kg):</b> ${pet.weightKg}</p>
        </div>

        
       <!-- 이전 버튼 -->
<c:if test="${prevExists}">
    <form action="main.do" method="get" style="display:inline;">
        <input type="hidden" name="petId" value="${prevPetId}">
        <button type="submit">◀ 이전</button>
    </form>
</c:if>

<!-- 다음 버튼 -->
<c:if test="${nextExists}">
    <form action="main.do" method="get" style="display:inline; margin-left:5px;">
        <input type="hidden" name="petId" value="${nextPetId}">
        <button type="submit">다음 ▶</button>
    </form>
</c:if>

<!-- 다음 없음 → 추가 버튼 -->
<c:if test="${!nextExists}">
    <button type="button" style="margin-left:5px;"
            onclick="location.href='${pageContext.request.contextPath}/pet/add.do'">
        ➕ 추가하기
    </button>
</c:if>


    </c:when>
    <c:otherwise>
        <h3>등록된 반려견이 없습니다.</h3>
    </c:otherwise>
</c:choose>


<hr>
<h2>펫케어 바로가기</h2>
<p>운동 기록과 투약 기록, 비만도 체크를 바로 이용해 보세요.</p>

<div class="care-grid">
  <section class="care-card">
    <h3>운동 관리</h3>
    <p>운동 기록을 남기고 리스트를 확인하세요.</p>
    <c:if test="${pet != null}">
        <a href="${pageContext.request.contextPath}/exer/list.do?petId=${pet.petId}">운동 기록 보기</a>
    </c:if>
    <c:if test="${empty exerList}">
        <p>운동 기록이 없습니다. 추가해보세요!</p>
    </c:if>
  </section>

  <section class="care-card">
    <h3>투약 기록</h3>
    <p>투약 기록을 조회하고 새 기록을 등록하세요.</p>
    <a href="<c:url value='/medi/list.do'/>">투약 목록</a>
    <a href="<c:url value='/medi/add.do'/>">투약 등록</a>
  </section>

  <section class="care-card obesity-card">
      <h3>비만도 체크</h3>
      <p>간단한 체크리스트로 비만도(BCS)를 확인해 보세요.</p>
      <form id="obesityForm">
        <label>반려동물 ID
          <input type="number" name="petId" id="obesityPetId" min="1" required>
        </label>
        <fieldset>
          <legend>체크리스트</legend>
          <div class="question">1) 갈비뼈가 뚜렷하게 만져지나요?
            <label><input type="radio" name="check_1" value="Y"> 예</label>
            <label><input type="radio" name="check_1" value="N" checked> 아니오</label>
          </div>
          <div class="question">2) 허리선과 배선이 잘 구분되나요?
            <label><input type="radio" name="check_2" value="Y"> 예</label>
            <label><input type="radio" name="check_2" value="N" checked> 아니오</label>
          </div>
          <div class="question">3) 위에서 보았을 때 모래시계 모양인가요?
            <label><input type="radio" name="check_3" value="Y"> 예</label>
            <label><input type="radio" name="check_3" value="N" checked> 아니오</label>
          </div>
          <div class="question">4) 옆에서 보았을 때 복부가 안쪽으로 들어갔나요?
            <label><input type="radio" name="check_4" value="Y"> 예</label>
            <label><input type="radio" name="check_4" value="N" checked> 아니오</label>
          </div>
          <div class="question">5) 활동량이 충분한 편인가요?
            <label><input type="radio" name="check_5" value="Y"> 예</label>
            <label><input type="radio" name="check_5" value="N" checked> 아니오</label>
          </div>
        </fieldset>
        <button type="submit" class="primary">체크하기</button>
        <button type="button" onclick="viewLatest()" class="primary">최근 결과 보기</button>
      </form>
      <div id="obesityResult" class="result-box"></div>
    </section>
</div>
</main>

<footer>© 2025 PetCare Mini Project | Team petcare</footer>

<script>
// 기존 JS 그대로
</script>
</body>
</html>
