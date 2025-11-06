<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${post.title}</title>
<style>
    body {
        font-family: sans-serif;
        margin: 0;
        padding: 0;
    }

    /* ① 네비게이션 */
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
        width: 70%;
        margin: 40px auto;
    }

    ul { list-style: none; padding: 0; margin: 0; }

    /* 제목 + 버튼 한 줄 */
    .post-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 5px;
    }

    .post-header h2 {
        margin: 0;
        font-size: 18px;
        flex: 1;
    }

    /* 버튼 텍스트형 */
    .button-group {
        display: flex;
        gap: 15px;
    }

    .button-group a {
        text-decoration: none;
        font-size: 14px;
    }

    hr {
        border: none;
        border-top: 1px solid #000;
        margin: 10px 0 15px 0;
    }

    /* 게시글 정보 (No / 작성일 / 작성자 등) */
    .post-info {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        gap: 30px;
        margin-bottom: 20px;
        font-size: 14px;
    }

    /* 본문 */
    .post-content {
        white-space: pre-wrap;
        line-height: 1.6;
        margin-bottom: 30px;
    }

    /* 댓글 제목 */
    .comment-title {
        font-weight: bold;
        margin-bottom: 8px;
        margin-top: 25px;
    }

    /* 댓글 입력 */
    .comment-form textarea {
        width: 100%;
        height: 80px;
        resize: none;
        border: none;
        border-bottom: 1px solid #ccc;
        padding: 8px;
        font-family: sans-serif;
        font-size: 14px;
        box-sizing: border-box;
        outline: none;
    }

    .comment-form input[type="submit"] {
        margin-top: 8px;
        padding: 5px 15px;
        float: right;
        cursor: pointer;
    }

    /* 댓글 리스트 */
    .comment-list li {
        padding: 5px 0;
        border-bottom: 1px solid #eee;
    }

    /* Footer */
    footer {
        text-align: center;
        padding: 20px;
        border-top: 1px solid #ccc;
        font-size: 14px;
        margin-top: 50px;
    }
</style>
</head>
<body>

<!-- ① 네비게이션 -->
<header>
    <h1>PET CARE</h1>
    <nav>
        <a href="#">home</a>
        <a href="${pageContext.request.contextPath}/post/list.do">community</a>
        <a href="#">care</a>
        <a href="#">login</a>
    </nav>
</header>

<!-- 본문 -->
<main>

    <!-- 제목 + 버튼 -->
    <div class="post-header">
        <h2>No.${post.postId}　${post.title}</h2>
        <ul class="button-group">
            <li><a href="${pageContext.request.contextPath}/post/edit.do?post_id=${post.postId}">수정하기</a></li>
            <li><a href="${pageContext.request.contextPath}/post/delete.do?post_id=${post.postId}"
                   onclick="return confirm('정말 삭제하시겠습니까?');">삭제하기</a></li>
            <li><a href="${pageContext.request.contextPath}/post/list.do">글목록</a></li>
        </ul>
    </div>

    <hr>

    <!-- 게시글 정보 -->
    <ul class="post-info">
        <li>${post.createdAt}</li>
        <li>작성자 : ${post.ownerNickname}</li>
        <li>댓글 : ${post.commentCount}</li>
        <li>좋아요 : ${post.likeCount}</li>
        <li>조회수 : ${post.viewCount}</li>
    </ul>

    <!-- 본문 -->
    <div class="post-content">
        ${post.content}
    </div>


    <!-- 댓글 영역 -->
    <div class="comment-area">
        <div class="comment-title">댓글</div>

        <!-- 댓글 입력 -->
        <form method="post" action="${pageContext.request.contextPath}/comment.do" class="comment-form">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="post_id" value="${post.postId}">
            <textarea name="content" placeholder="댓글을 입력하세요." required></textarea><br>
            <input type="submit" value="등록">
        </form>

        <!-- 댓글 목록 -->
        <ul class="comment-list">
            <c:if test="${empty comments}">
                <li></li>
            </c:if>
            <c:forEach var="cmt" items="${comments}">
                <li>
                    <strong>${cmt.nickname}</strong>
                    <small>(${cmt.created_at})</small><br>
                    ${cmt.content}
                </li>
            </c:forEach>
        </ul>
    </div>

</main>

<!-- Footer -->
<footer>
    <p>📍 PetCare 서비스 안내</p>
    <p>반려동물의 건강을 관리하고,<br>보호자 간의 소통을 돕는 종합 케어 플랫폼입니다.</p>
    <p>📞 고객센터 : 010-1234-5678 | ✉️ 이메일 : petcare_team4@gmail.com</p>
    <p>🏢 주소 : 서울특별시 강남구 테헤란로 100, PetCare 센터</p>
    <p>이용약관 | 개인정보처리방침 | 관리자 로그인</p>
    <p>ⓒ 2025 PetCare Mini Project | Team 4</p>
</footer>

</body>
</html>
