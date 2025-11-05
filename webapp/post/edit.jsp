<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<style>
    body {
        font-family: sans-serif;
        margin: 0;
        padding: 0;
    }

    header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 40px;
        border-bottom: 1px solid #ccc;
    }

    header nav a {
        margin: 0 10px;
        text-decoration: none;
    }

    main {
        width: 60%;
        margin: 40px auto;
    }

    h2 {
        text-align: left;
        margin-bottom: 30px;
        border-bottom: 2px solid #000;
        padding-bottom: 10px;
    }

    form {
        margin-bottom: 20px;
    }

    form p {
        margin: 10px 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    label {
        flex: 1;
        text-align: right;
        margin-right: 15px;
    }

    input[type="text"],
    textarea,
    input[type="file"] {
        flex: 3;
        padding: 6px;
        border: 1px solid #ccc;
    }

    textarea {
        resize: none;
        height: 200px;
    }

    .btn-area {
        text-align: center;
        margin-top: 20px;
    }

    input[type="submit"],
    a.button {
        display: inline-block;
        padding: 8px 16px;
        margin: 5px;
        border: 1px solid #aaa;
        background: #f9f9f9;
        text-decoration: none;
        cursor: pointer;
    }

    input[type="submit"]:hover,
    a.button:hover {
        background: #eee;
    }

    hr {
        margin: 30px 0;
        border: 0;
        border-top: 1px solid #ccc;
    }

    footer {
        text-align: center;
        padding: 20px;
        border-top: 1px solid #ccc;
        margin-top: 50px;
        font-size: 14px;
    }
</style>
</head>
<body>

<header>
    <h1>PET CARE</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/post/list.do">Community</a>
        <a href="#">Care</a>
        <a href="#">My Page</a>
    </nav>
</header>

<main>
    <h2>게시글 수정</h2>

    <!-- ✏️ 수정 폼 -->
    <form method="post" action="${pageContext.request.contextPath}/post/edit.do" enctype="multipart/form-data">
        <input type="hidden" name="post_id" value="${post.postId}">
        <input type="hidden" name="action" value="update">

        <p>
            <label>제목:</label>
            <input type="text" name="title" value="${post.title}" required>
        </p>

        <p style="align-items:flex-start;">
            <label>내용:</label>
            <textarea name="content" rows="10" cols="60">${post.content}</textarea>
        </p>

        <p>
            <label>첨부파일 변경:</label>
            <input type="file" name="file">
        </p>

        <div class="btn-area">
            <input type="submit" value="수정 완료">
        </div>
    </form>

    <hr>

    <!-- 🗑 삭제 폼 -->
    <form method="post" action="${pageContext.request.contextPath}/post/edit.do" style="text-align:center;">
        <input type="hidden" name="post_id" value="${post.postId}">
        <input type="hidden" name="action" value="delete">
        <input type="submit" value="삭제하기" onclick="return confirm('정말 삭제하시겠습니까?');">
    </form>

    <!-- 🔙 하단 네비 -->
    <div class="btn-area">
        <a href="${pageContext.request.contextPath}/post/view.do?post_id=${post.postId}" class="button">뒤로</a>
        <a href="${pageContext.request.contextPath}/post/list.do" class="button">목록</a>
    </div>
</main>

<footer>
    <p>📍 PetCare 서비스 안내</p>
    <p>반려동물의 건강을 관리하고 보호자 간의 소통을 돕는 종합 케어 플랫폼입니다.</p>
    <p>📞 010-1234-5678 | ✉️ petcare_team4@gmail.com</p>
    <p>ⓒ 2025 PetCare Mini Project | Team 4</p>
</footer>

</body>
</html>
