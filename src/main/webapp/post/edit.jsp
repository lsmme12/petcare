<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
</head>
<body>

<h2>게시글 수정</h2>

<form method="post" action="${pageContext.request.contextPath}/post/edit.do" enctype="multipart/form-data">
    <input type="hidden" name="post_id" value="${post.postId}">
    <input type="hidden" name="action" value="update">
    <p>제목: <input type="text" name="title" value="${post.title}" required></p>
    <p>내용: <textarea name="content" rows="10" cols="60">${post.content}</textarea></p>
    <p>첨부파일 변경: <input type="file" name="file"></p>
    <p><input type="submit" value="수정 완료"></p>
</form>

<hr>

<form method="post" action="${pageContext.request.contextPath}/post/edit.do">
    <input type="hidden" name="post_id" value="${post.postId}">
    <input type="hidden" name="action" value="delete">
    <input type="submit" value="삭제하기" onclick="return confirm('정말 삭제하시겠습니까?');">
</form>

<a href="${pageContext.request.contextPath}/post/view.do?post_id=${post.postId}">뒤로</a> |
<a href="${pageContext.request.contextPath}/post/list.do">목록</a>

</body>
</html>
