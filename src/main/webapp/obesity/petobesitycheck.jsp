<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
  학생 가이드
  - 이 페이지는 간단한 프론트엔드 기반 BCS(비만도) 체크 예제입니다.
  - 자바스크립트로 점수를 계산해 화면에 보여주고, ObesityServlet("/obesity/create")로 제출합니다.
  - 팀 표준 라우팅(서블릿)과 함께 사용할 수 있도록 폴더명을 obesity로 정리했습니다.
--%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>강아지 비만도 체크 & 저장</title>
<style>
  body { font-family: system-ui, sans-serif; max-width: 760px; margin: 24px auto; }
  .q { margin: 10px 0; }
  .row { display:flex; align-items:center; gap:8px; }
  .out { margin-top: 14px; padding:12px; border:1px solid #e5e7eb; border-radius:12px; display:none; }
</style>
</head>
<body>
<h1>강아지 비만도 체크 & 저장</h1>
<form id="frm" method="post" action="<%= request.getContextPath() %>/obesity/">
  <input type="hidden" name="_method" value="POST">
  <div class="row">
    <label>반려견 ID:</label>
    <input type="number" name="petId" id="petId" min="1" required>
  </div>

  <div class="q">1) 갈비뼈가 잘 만져지지 않는다
    <label><input type="radio" name="check_1" value="Y">예</label>
    <label><input type="radio" name="check_1" value="N" checked>아니오</label>
  </div>
  <div class="q">2) 위에서 보았을 때 허리 선이 없다
    <label><input type="radio" name="check_2" value="Y">예</label>
    <label><input type="radio" name="check_2" value="N" checked>아니오</label>
  </div>
  <div class="q">3) 옆에서 보았을 때 복부 라인이 아래로 처졌다
    <label><input type="radio" name="check_3" value="Y">예</label>
    <label><input type="radio" name="check_3" value="N" checked>아니오</label>
  </div>
  <div class="q">4) 목·가슴둘레가 두껍고 지방이 만져진다
    <label><input type="radio" name="check_4" value="Y">예</label>
    <label><input type="radio" name="check_4" value="N" checked>아니오</label>
  </div>
  <div class="q">5) 활동량이 적고 쉽게 지친다
    <label><input type="radio" name="check_5" value="Y">예</label>
    <label><input type="radio" name="check_5" value="N" checked>아니오</label>
  </div>

  <button type="button" onclick="calcAndSubmit()">계산하고 저장</button>
  <div id="out" class="out"></div>
</form>

<script>
function calcAndSubmit(){
  const get = (name)=>document.querySelector('input[name="'+name+'"]:checked').value;
  const pts = ['check_1','check_2','check_3','check_4','check_5']
               .map(n => get(n)==='Y' ? 2 : 0)
               .reduce((a,b)=>a+b,0);
  let label = '';
  if (pts<=2) label = '저체중';
  else if (pts===4) label = '정상(슬림)';
  else if (pts===6) label = '정상~과체중 경계';
  else if (pts===8) label = '과체중';
  else if (pts===10) label = '비만';
  else label = '판단불가';

  const out = document.getElementById('out');
  out.style.display='block';
  out.innerHTML = '<b>점수: '+pts+' / 10</b><br>추정 BCS: '+label+'<br>서버에 저장합니다...';

  // submit (서버 트리거가 total_score, bcs_grade를 자동 계산)
  document.getElementById('frm').action = '<%= request.getContextPath() %>/obesity/create';
  document.getElementById('frm').submit();
}
</script>
</body>
</html>
