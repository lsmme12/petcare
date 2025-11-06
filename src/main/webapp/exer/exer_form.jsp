<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>운동 기록 추가</title>
</head>
<body>
    <h2>운동 기록 추가</h2>

    <!-- URL에서 petId 가져오기, 없으면 1로 기본값 -->
    <c:set var="petIdValue" value="${not empty param.petId ? param.petId : 1}" />

    <!-- 등록 폼 -->
    <form action="${pageContext.request.contextPath}/exer/insert.do" method="post">
        <input type="hidden" name="petId" value="${pet.petId}">
<p>반려동물: <strong>${pet.petName}</strong></p>


        <label>운동 날짜: </label>
        <input type="date" name="exer_date" required><br><br>

        <label>운동 시간(분): </label>
        <input type="number" name="exer_time" required><br><br>

        <label>난이도: </label>
        <select name="exer_level">
            <option value="쉬움">쉬움</option>
            <option value="보통">보통</option>
            <option value="어려움">어려움</option>
        </select><br><br>

        <label>메모: </label><br>
        <textarea name="memo" rows="4" cols="40"></textarea><br><br>

        <button type="submit">등록</button>
    </form>

    <br>
    <a href="${pageContext.request.contextPath}/exer/list.do?petId=${pet.petId}">목록으로</a>
</body>
</html>
