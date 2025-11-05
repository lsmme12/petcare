<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 중복확인</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>">
<script>
function applyId(id){
  if(window.opener && !window.opener.closed){
    const el = window.opener.document.getElementById('userid');
    if(el){ el.value = id; el.focus(); }
  }
  window.close();
}
</script>
</head>
<body>
<div class="popup">
  <h4>아이디 중복확인</h4>
  <c:choose>
    <c:when test="${result eq 'OK'}">
      <p><span class="ok">사용 가능한 아이디</span>입니다.</p>
      <p>입력하신 아이디: <strong>${userid}</strong></p>
      <div class="btns">
        <button type="button" onclick="applyId('${userid}')">이 아이디 사용</button>
        <button type="button" onclick="window.close()">닫기</button>
      </div>
    </c:when>
    <c:when test="${result eq 'FAIL'}">
      <p><span class="fail">이미 사용 중인 아이디</span>입니다.</p>
      <div class="btns"><button type="button" onclick="window.close()">확인</button></div>
    </c:when>
    <c:otherwise>
      <p>요청이 올바르지 않습니다.</p>
      <div class="btns"><button type="button" onclick="window.close()">닫기</button></div>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
