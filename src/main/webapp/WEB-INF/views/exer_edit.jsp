<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>운동 기록 수정</h2>

<form action="${pageContext.request.contextPath}/exer_update.do" method="post">
    <input type="hidden" name="exer_id" value="${record.exerId}">

    <p>운동 날짜: <input type="date" name="exer_date" value="${record.exerDate}" required></p>
    <p>운동 시간(분): <input type="number" name="exer_time" value="${record.exerTime}" required></p>
    
    <label>난이도: </label>
    <select name="exer_level">
        <option value="쉬움" ${record.exerLevel == '쉬움' ? 'selected' : ''}>쉬움</option>
        <option value="보통" ${record.exerLevel == '보통' ? 'selected' : ''}>보통</option>
        <option value="어려움" ${record.exerLevel == '어려움' ? 'selected' : ''}>어려움</option>
    </select><br>
    
    <p>메모: <textarea name="memo" rows="4" cols="40">${record.memo}</textarea></p>

    <button type="submit">수정 완료</button>
    <a href="${pageContext.request.contextPath}/exer_list.do">취소</a>
</form>

