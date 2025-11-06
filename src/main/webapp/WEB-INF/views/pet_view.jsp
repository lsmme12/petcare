<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>


<c:if test="${pet != null}">
    <div style="border:1px solid #ccc; padding:10px; width:300px;">
        <p><b>이름:</b> ${pet.petName}</p>
        <p><b>성별:</b> ${pet.sex}</p>
        <p><b>생년월일:</b> ${pet.birthDate}</p>
        <p><b>체중(kg):</b> ${pet.weightKg}</p>
    </div>

    <form action="pet_view.do" method="get" style="margin-top:10px;">
        <c:choose>
            <c:when test="${nextExists}">
                <input type="hidden" name="petId" value="${pet.petId + 1}" />
                <button type="submit">다음</button>
            </c:when>
            <c:otherwise>
                <button type="button" onclick="alert('추가하기')">추가하기</button>
            </c:otherwise>
        </c:choose>
    </form>
</c:if>

<c:if test="${pet == null}"><h3>DEBUG: ${pet}</h3>
    <p>등록된 반려견이 없습니다.</p>
    <button type="button" onclick="alert('추가하기')">추가하기</button>
</c:if>
