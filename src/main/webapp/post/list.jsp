<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>커뮤니티 게시판</title>
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

    /* ② 제목 + 검색 */
    h2 {
        margin: 30px 40px 10px;
    }

    .top-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin: 0 40px 15px;
    }

    /* 🔹 검색창 길이 늘림 */
    .top-bar form {
        flex: 1;
        display: flex;
        gap: 8px;
    }

    .top-bar input[type="text"] {
        flex: 1;
        padding: 6px 10px;
        font-size: 14px;
    }

    .top-bar input[type="submit"] {
        padding: 6px 15px;
        font-size: 14px;
        cursor: pointer;
    }

    /* 글쓰기 버튼 오른쪽 */
    .top-bar a {
        margin-left: 20px;
        text-decoration: none;
    }

    /* ③ 게시판 목록 */
    .post-list {
        width: 90%;
        margin: 0 auto;
        list-style: none;
        padding: 0;
        border-top: 2px solid #000;
    }

    .post-list ul {
        display: flex;
        list-style: none;
        padding: 10px 0;
        margin: 0;
        border-bottom: 1px solid #ccc;
    }

    .post-list li {
        flex: 1;
        text-align: center;
    }

    .post-list li.title {
        flex: 3;
        text-align: left;
        padding-left: 10px;
    }

    .header {
        font-weight: bold;
        border-bottom: 2px solid #000;
    }

    /* ④ 페이지 번호 */
    .pagination {
        text-align: center;
        margin: 20px 0;
    }

    /* ⑤ 푸터 */
    footer {
        text-align: center;
        padding: 20px;
        border-top: 1px solid #ccc;
        font-size: 14px;
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

<!-- ② 제목 + 검색 -->
<h2>COMMUNITY</h2>

<div class="top-bar">
    <form method="get" action="${pageContext.request.contextPath}/post/list.do">
        <input type="text" name="search" placeholder="글 검색">
        <input type="submit" value="검색">
    </form>

    <a href="${pageContext.request.contextPath}/post/write.do">📝 글쓰기</a>
</div>

<!-- ③ 게시글 목록 -->
<div class="post-list">
    <!-- 헤더 -->
    <ul class="header">
        <li>No.</li>
        <li class="title">제목</li>
        <li>작성자</li>
        <li>댓글</li>
        <li>좋아요</li>
        <li>작성일</li>
        <li>조회수</li>
    </ul>

    <!-- 게시글이 없을 때 -->
    <c:if test="${empty postList}">
        <ul>
            <li style="flex:7; text-align:center;">등록된 게시글이 없습니다.</li>
        </ul>
    </c:if>

    <!-- 게시글 목록 -->
    <c:set var="startNo" value="${totalCount - (page-1) * limit}" />
    <c:forEach var="post" items="${postList}" varStatus="st">
        <ul>
            <li>${startNo - st.index}</li>   <!-- ✅ 게시글 번호 -->
            <li class="title">
                <a href="${pageContext.request.contextPath}/post/view.do?post_id=${post.postId}">
                    ${post.title}
                </a>
            </li>
            <li>${post.ownerNickname}</li>
            <li>${post.commentCount}</li>
            <li>${post.likeCount}</li>
            <li>${post.createdAt}</li>
            <li>${post.viewCount}</li>
        </ul>
    </c:forEach>
</div>

<!-- ④ 페이지 번호 -->
<div class="pagination">
    <a href="#">◀ Previous</a>
    <a href="#">1</a>
    <a href="#">2</a>
    <a href="#">3</a>
    <a href="#">Next ▶</a>
</div>



</body>
</html>
