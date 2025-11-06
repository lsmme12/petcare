<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h2>운동 기록 수정</h2>

<form action="${pageContext.request.contextPath}/exer_update.do" method="post">
    <input type="hidden" name="exer_id" value="${record.exerId}" />

    <label>운동 날짜:</label>
    <input type="date" name="exer_date" value="${record.exerDate}" /><br>

    <label>운동 시간(분):</label>
    <input type="number" name="exer_time" value="${record.exerTime}" /><br>

    <label>난이도:</label>
    <input type="text" name="exer_level" value="${record.exerLevel}" /><br>

    <label>메모:</label><br>
    <textarea name="memo">${record.memo}</textarea><br><br>

    <button type="submit">수정 완료</button>
    <a href="${pageContext.request.contextPath}/exer_list.do">목록으로</a>
</form>
