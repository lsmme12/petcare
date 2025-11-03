<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>

<h2>게시글 작성</h2>

<form method="post" action="${pageContext.request.contextPath}/post/write.do" enctype="multipart/form-data">
    <p>제목: <input type="text" name="title" required></p>
    <p>내용: <textarea name="content" rows="10" cols="60" required></textarea></p>
    <p>첨부파일: <input type="file" name="file"></p>
    <p><input type="submit" value="등록"></p>
</form>

<a href="${pageContext.request.contextPath}/post/list.do">목록으로</a>

</body>
</html>
