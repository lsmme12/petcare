<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h2>운동 기록 리스트</h2>
<h2>운동 기록 목록</h2>

<a href="${pageContext.request.contextPath}/exer_form.do">➕ 새 운동기록 추가</a>
<table border="1">
    <tr>
        <th>ID</th><th>펫번호</th><th>운동날짜</th><th>운동시간</th><th>난이도</th><th>메모</th><th>관리</th>
    </tr>
    <c:forEach var="rec" items="${list}">
        <tr>
            <td>${rec.exerId}</td>
            <td>${rec.petId}</td>
            <td>${rec.exerDate}</td>
            <td>${rec.exerTime}</td>
            <td>${rec.exerLevel}</td>
            <td>${rec.memo}</td>
            <td>
                <a href="${pageContext.request.contextPath}/exer_edit.do?exer_id=${rec.exerId}">수정</a> |
                <a href="${pageContext.request.contextPath}/exer_delete.do?exer_id=${rec.exerId}"
                   onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
            </td>
        </tr>
    </c:forEach>
</table>


<br>
<a href="${pageContext.request.contextPath}/exer_form.do">등록하기</a>



