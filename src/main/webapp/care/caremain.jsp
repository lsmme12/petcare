<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>PetCare | 케어 허브</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
  <style>
    .care-grid { display:flex; gap:16px; justify-content:center; margin-top:32px; flex-wrap:wrap; }
    .care-card { background:#fff2c2; border-radius:16px; padding:20px 24px; min-width:260px; max-width:320px; box-shadow:0 2px 8px rgba(0,0,0,.06); text-align:left; }
    .care-card h3 { margin:0 0 10px; color:#4a2c18; font-size:20px; }
    .care-card p { margin:0 0 14px; color:#5b4030; font-size:14px; line-height:1.5; }
    .care-card a, .care-card button.primary { display:inline-block; margin:6px 10px 0 0; background:#5c2e10; color:#fff; text-decoration:none; padding:8px 14px; border-radius:18px; font-weight:600; border:none; cursor:pointer; }
    .care-card a:hover, .care-card button.primary:hover { background:#7a3d14; }
    .obesity-card form { display:flex; flex-direction:column; gap:10px; margin-top:14px; }
    .obesity-card label { font-size:13px; color:#4a2c18; }
    .obesity-card .question { display:flex; flex-wrap:wrap; gap:8px; }
    .obesity-card fieldset { border:none; padding:0; margin:0; }
    .result-box { margin-top:12px; padding:12px; border-radius:12px; background:#fff8e6; border:1px solid #f2d3a2; display:none; }
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
  <h2>펫케어 바로가기</h2>
  <p>운동 기록과 투약 기록, 비만도 체크를 바로 이용해 보세요.</p>

  <div class="care-grid">
    <section class="care-card">
      <h3>운동 관리</h3>
      <p>운동 기록을 남기고 리스트를 확인하세요.</p>
      <a href="<c:url value='/exer_list.do'/>">운동 목록</a>
      <a href="<c:url value='/exer_form.do'/>">운동 등록</a>
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
  const ctxPath = '${pageContext.request.contextPath}';
  document.getElementById('obesityForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const form = e.target;
    const scoreFields = ['check_1','check_2','check_3','check_4','check_5'];
    const scores = scoreFields.map(function (name) {
      var selected = form.querySelector('input[name="' + name + '"]:checked');
      return selected && selected.value === 'Y' ? 2 : 0;
    });
    var total = scores.reduce(function (sum, val) { return sum + val; }, 0);
    var grade = '정상 범위';
    if (total <= 2) grade = '저체중';
    else if (total === 4) grade = '정상 (마른)';
    else if (total === 6) grade = '정상~과체중 경계';
    else if (total === 8) grade = '과체중';
    else if (total >= 10) grade = '비만';

    var resultBox = document.getElementById('obesityResult');
    resultBox.style.display = 'block';
    resultBox.innerHTML = '<strong>점수: ' + total + ' / 10</strong><br>' +
                          '추정 BCS: ' + grade + '<br>' +
                          '운동과 식단을 조절해 주세요.';
  });

  function viewLatest() {
    var petId = document.getElementById('obesityPetId').value;
    if (!petId) {
      alert('반려동물 ID를 입력해 주세요.');
      return;
    }
    window.location.href = ctxPath + '/obesity/latest.do?petId=' + encodeURIComponent(petId);
  }
</script>
</body>
</html>
