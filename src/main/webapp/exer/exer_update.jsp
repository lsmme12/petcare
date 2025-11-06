<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h2>운동 기록 수정</h2>

<form action="${pageContext.request.contextPath}/exer/update.do" method="post">
    <input type="hidden" name="exer_id" value="${record.exerId}" />
    <input type="hidden" name="pet_id" value="${record.petId}">
    

    <label>운동 날짜:</label>
    <input type="date" name="exer_date" value="${record.exerDate}" /><br>

    <label>운동 시간(분):</label>
    <input type="number" name="exer_time" value="${record.exerTime}" /><br>

    <label>난이도:</label>
<select name="exer_level">
    <option value="쉬움" ${record.exerLevel == '쉬움' ? 'selected' : ''}>쉬움</option>
    <option value="보통" ${record.exerLevel == '보통' ? 'selected' : ''}>보통</option>
    <option value="어려움" ${record.exerLevel == '어려움' ? 'selected' : ''}>어려움</option>
</select><br>


    <label>메모:</label><br>
    <textarea name="memo">${record.memo}</textarea><br><br>

    <button type="submit">수정 완료</button>
    <a href="${pageContext.request.contextPath}/exer/list.do">목록으로</a>
</form>
