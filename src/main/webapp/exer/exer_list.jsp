<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h3>
    <c:choose>
        <c:when test="${pet != null}">${pet.petName} 운동 기록</c:when>
        <c:otherwise>운동 기록</c:otherwise>
    </c:choose>
</h3>

<!-- 소유자가 아닌 경우 -->
<c:if test="${ownerMismatch}">
    <p style="color:red;">⚠️ 이 반려동물의 운동 기록을 추가할 수 없습니다. 소유자가 아닙니다.</p>
</c:if>

<!-- 정상 접근: 운동 기록 추가 버튼 -->
<c:if test="${!ownerMismatch && pet != null}">
    <a href="${pageContext.request.contextPath}/exer/form.do?petId=${pet.petId}">
         운동 기록 추가
    </a>
</c:if>

<!-- 운동 기록 테이블 -->
<table border="1" cellpadding="5">
    <tr>
        <th>날짜</th>
        <th>운동 시간(분)</th>
        <th>난이도</th>
        <th>메모</th>
        <th>관리</th>
    </tr>
    <c:forEach var="rec" items="${exerList}">
        <tr>
            <td>${rec.exerDate}</td>
            <td>${rec.exerTime}</td>
            <td>${rec.exerLevel}</td>
            <td>${rec.memo}</td>
            <td>
                <form action="${pageContext.request.contextPath}/exer/edit.do" method="get" style="display:inline;">
    <input type="hidden" name="exerId" value="${rec.exerId}">
    <input type="hidden" name="petId" value="${pet.petId}">
    <button type="submit">수정</button>
</form>
                <form action="${pageContext.request.contextPath}/exer/delete.do" method="post" style="display:inline;">
                    <input type="hidden" name="exerId" value="${rec.exerId}">
                    <input type="hidden" name="petId" value="${pet.petId}">
                    <button type="submit" onclick="return confirm('삭제할까요?');">삭제</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<!-- 운동 기록이 없으면 안내 메시지 -->
<c:if test="${!ownerMismatch && empty exerList}">
    <p>운동 기록이 없습니다. 추가해보세요!</p>
</c:if>

<!-- 뒤로가기 -->
<c:if test="${pet != null}">
    <a href="${pageContext.request.contextPath}/care/main.do?petId=${pet.petId}">돌아가기</a>
</c:if>
