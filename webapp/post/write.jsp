<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
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

    /* 전체 레이아웃 */
    main {
        width: 70%;
        margin: 40px auto;
    }

    h2 {
        margin-bottom: 30px;
    }

    /* UL 기본 정렬 제거 */
    ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    /* 각 입력 영역 li */
    li {
        margin-bottom: 20px;
        display: flex;
        flex-direction: column;
    }

    label {
        margin-bottom: 5px;
        font-weight: bold;
    }

    input[type="file"],
    input[type="password"],
    input[type="text"],
    textarea {
        padding: 8px;
        border: 1px solid #ccc;
        font-size: 14px;
        width: 100%;
        box-sizing: border-box;
    }

    textarea {
        height: 250px;
        resize: none;
    }

    /* 첨부파일 + 비밀번호 한 줄 */
    .file-password {
        display: flex;
        justify-content: space-between;
        gap: 20px;
    }
    .file-password li {
        flex: 1;
    }

    /* 등록 버튼 오른쪽 정렬 */
    .submit-area {
        text-align: right;
    }

    input[type="submit"] {
        padding: 8px 20px;
        font-size: 14px;
        cursor: pointer;
    }

    /* ⑤ 푸터 */
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
        <a href="#">logout</a>
    </nav>
</header>

<!-- ② 게시글 작성 폼 -->
<main>
    <h2>게시글 작성</h2>

    <form method="post" action="${pageContext.request.contextPath}/post/write.do" enctype="multipart/form-data">
        <ul>
            <!-- 첨부파일 + 비밀번호 -->
            <li>
                <ul class="file-password">
                    <li>
                        <label>첨부파일</label>
                        <input type="file" name="uploadFile">
                    </li>
                    <li>
                        <label>비밀번호</label>
                        <input type="password" name="postPwd" placeholder="4자리 이상 입력하세요.">
                    </li>
                </ul>
            </li>

            <!-- 제목 -->
            <li>
                <label>제목</label>
                <input type="text" name="title" placeholder="제목을 입력하세요." required>
            </li>

            <!-- 내용 -->
            <li>
                <label>내용</label>
                <textarea name="content" placeholder="내용을 입력하세요." required></textarea>
            </li>

            <!-- 등록 버튼 -->
            <li class="submit-area">
                <input type="submit" value="등록">
            </li>
        </ul>
    </form>
</main>

<!-- ⑤ Footer -->
<footer>
    <p>📍 PetCare 서비스 안내</p>
    <p>반려동물의 건강을 관리하고,<br>보호자 간의 소통을 돕는 종합 케어 플랫폼입니다.</p>
    <p>☎ 고객센터: 010-1234-5678 | ✉ petcare.team4@gmail.com</p>
    <p>© 2025 PetCare Mini Project | Team 4</p>
</footer>

</body>
</html>
