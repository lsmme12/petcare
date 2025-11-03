<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${post.title}</title>
</head>
<body>

<h2>${post.title}</h2>
<p>작성자: ${post.ownerNickname}</p>
<p>작성일: ${post.createdAt}</p>
<p>조회수: ${post.viewCount}</p>
<p>좋아요: ${post.likeCount}</p>
<hr>
<pre>${post.content}</pre>
<hr>

<!-- ❤️ 좋아요 버튼 -->
<form method="post" action="${pageContext.request.contextPath}/post/view.do">
    <input type="hidden" name="post_id" value="${post.postId}">
    <input type="submit" value="좋아요 ❤️">
</form>

<!-- ✏️ 수정/삭제 버튼 -->
<a href="${pageContext.request.contextPath}/post/edit.do?post_id=${post.postId}">수정</a> |
<a href="${pageContext.request.contextPath}/post/list.do">목록으로</a>
<hr>
<h3>💬 댓글 목록</h3>

<c:if test="${empty comments}">
    <p>아직 댓글이 없습니다 😿</p>
</c:if>

<c:if test="${not empty comments}">
<table border="1" width="100%">
    <tr>
        <th>작성자</th>
        <th>내용</th>
        <th>작성일</th>
    </tr>
    <c:forEach var="cmt" items="${comments}">
        <tr>
            <td>${cmt.nickname}</td>
            <td>${cmt.content}</td>
            <td>${cmt.created_at}</td>
        </tr>
    </c:forEach>
</table>
</c:if>
<h3>✏️ 댓글 작성</h3>

<form method="post" action="${pageContext.request.contextPath}/comment/write.do">
    <input type="hidden" name="post_id" value="${post.postId}">
    <textarea name="content" rows="3" cols="60" placeholder="댓글을 입력하세요" required></textarea><br>
    <input type="submit" value="등록">
</form>

</body>
</html>
