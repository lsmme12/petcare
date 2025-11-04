<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<body>

<h2>게시글 목록</h2>

<!-- ✅ 글쓰기 버튼 -->
<p style="text-align:right;">
    <a href="${pageContext.request.contextPath}/post/write.do">📝 새 글 작성</a>
</p>

<table>
    <tr>
        <th width="5%">번호</th>
        <th width="35%">제목</th>
        <th width="10%">작성자</th>
        <th width="10%">좋아요</th>
        <th width="10%">조회수</th>
        <th width="10%">댓글</th>
        <th width="20%">작성일</th>
    </tr>

    <!-- ✅ 게시글이 없을 때 -->
    <c:if test="${empty postList}">
        <tr>
            <td colspan="7" style="color:red;">등록된 게시글이 없습니다.</td>
        </tr>
    </c:if>

    <!-- ✅ 게시글 목록 출력 -->
    <c:forEach var="post" items="${postList}">
        <tr>
            <td>${post.postId}</td>
            <td>
                <a href="${pageContext.request.contextPath}/post/view.do?post_id=${post.postId}">
                    ${post.title}
                </a>
            </td>
            <td>${post.ownerNickname}</td>
            <td>${post.likeCount}</td>
            <td>${post.viewCount}</td>
            <td>${post.commentCount}</td>
            <td>${post.createdAt}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
