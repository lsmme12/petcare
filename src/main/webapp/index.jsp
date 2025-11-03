<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>🐾 JSP 미니 게시판</title>

<!-- ✅ 간단한 CSS 꾸미기 -->
<style>
body {
    font-family: 'Malgun Gothic', sans-serif;
    background: #f7f7f7;
    margin: 40px;
}
h2 {
    color: #333;
}
table {
    width: 100%;
    border-collapse: collapse;
    background: #fff;
}
th, td {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: center;
}
th {
    background: #eaeaea;
}
a {
    color: #0078d7;
    text-decoration: none;
}
a:hover {
    text-decoration: underline;
}
button, input[type=submit] {
    background: #0078d7;
    color: #fff;
    border: none;
    padding: 8px 12px;
    border-radius: 4px;
    cursor: pointer;
}
button:hover, input[type=submit]:hover {
    background: #005fa3;
}
textarea, input[type=text] {
    width: 80%;
    padding: 6px;
}
.container {
    max-width: 900px;
    margin: auto;
    background: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 8px rgba(0,0,0,0.1);
}
.home-btn {
    background: #444;
}
.home-btn:hover {
    background: #222;
}
</style>

<!-- ✅ 간단한 JavaScript (확인 메시지 등) -->
<script>
function confirmDelete() {
    return confirm("정말 삭제하시겠습니까?");
}
</script>
</head>

<body>
<div class="container">

<%
    /* ==========================================================
       📦 1. 전역 저장소 초기화 (가짜 DB 역할)
       ========================================================== */

    // 💾 게시글 전체를 저장하는 리스트 (Application 영역에 저장)
    if (application.getAttribute("posts") == null) {
        application.setAttribute("posts", new ArrayList<Map<String,Object>>());
    }
    List<Map<String,Object>> posts = (List<Map<String,Object>>) application.getAttribute("posts");

    // 💬 각 게시글의 댓글을 저장하는 맵 (post_id → 댓글목록)
    if (application.getAttribute("comments") == null) {
        application.setAttribute("comments", new HashMap<Integer, List<Map<String,Object>>>());
    }
    Map<Integer, List<Map<String,Object>>> commentsMap =
        (Map<Integer, List<Map<String,Object>>>) application.getAttribute("comments");

    /* ==========================================================
       📌 2. 요청 파라미터(mode/action/post_id) 처리
       ========================================================== */
    String mode = request.getParameter("mode");          // 화면 구분용: list / write / view / edit
    String action = request.getParameter("action");      // 실제 동작 구분용: insert / update / delete 등
    int postId = 0;                                      // 게시글 ID 번호
    if (request.getParameter("post_id") != null && !request.getParameter("post_id").isEmpty()) {
        postId = Integer.parseInt(request.getParameter("post_id"));
    }

    // 날짜 표시용 포맷
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /* ==========================================================
       ✏️ 3. 게시글 등록 기능
       ========================================================== */
    if ("insert".equals(action)) {
        Map<String,Object> post = new HashMap<>(); // 게시글 1개를 담을 Map 생성
        post.put("id", posts.size() + 1);          // 글 번호
        post.put("title", request.getParameter("title"));   // 제목
        post.put("author", request.getParameter("author")); // 작성자
        post.put("content", request.getParameter("content"));// 내용
        post.put("view", 0);                       // 조회수 초기값
        post.put("like", 0);                       // 좋아요 초기값
        post.put("created", sdf.format(new Date())); // 현재 시각
        posts.add(post);                            // 게시글 목록에 추가
        response.sendRedirect("index.jsp");         // 다시 목록으로 돌아가기
        return;
    }

    /* ==========================================================
       🛠 4. 게시글 수정 기능
       ========================================================== */
    if ("update".equals(action)) {
        for (Map<String,Object> post : posts) {
            if ((int)post.get("id") == postId) {
                post.put("title", request.getParameter("title"));   // 제목 변경
                post.put("content", request.getParameter("content"));// 내용 변경
                break;
            }
        }
        response.sendRedirect("index.jsp?mode=view&post_id=" + postId); // 수정 후 보기로 이동
        return;
    }

    /* ==========================================================
       🗑 5. 게시글 삭제 기능
       ========================================================== */
    if ("delete".equals(action)) {
        Iterator<Map<String,Object>> it = posts.iterator(); // 리스트 반복자 생성
        while (it.hasNext()) {
            Map<String,Object> p = it.next();
            if ((int)p.get("id") == postId) { // 글 번호 일치 시 삭제
                it.remove();
                break;
            }
        }
        response.sendRedirect("index.jsp"); // 삭제 후 목록으로
        return;
    }

    /* ==========================================================
       ❤️ 6. 좋아요 기능
       ========================================================== */
    if ("like".equals(action)) {
        for (Map<String,Object> post : posts) {
            if ((int)post.get("id") == postId) {
                int currentLike = (int)post.get("like"); // 현재 좋아요 수
                post.put("like", currentLike + 1);       // +1 증가
                break;
            }
        }
        response.sendRedirect("index.jsp?mode=view&post_id=" + postId); // 다시 보기 페이지로
        return;
    }

    /* ==========================================================
       💬 7. 댓글 등록 기능
       ========================================================== */
    if ("comment_insert".equals(action)) {
        String writer = request.getParameter("writer");
        String content = request.getParameter("content");

        List<Map<String,Object>> commentList = commentsMap.get(postId);
        if (commentList == null) {
            commentList = new ArrayList<>();
            commentsMap.put(postId, commentList);
        }

        Map<String,Object> comment = new HashMap<>();
        comment.put("id", commentList.size() + 1);
        comment.put("writer", writer);
        comment.put("content", content);
        comment.put("created", sdf.format(new Date()));
        commentList.add(comment);

        response.sendRedirect("index.jsp?mode=view&post_id=" + postId);
        return;
    }

    /* ==========================================================
       💬 8. 댓글 삭제 기능
       ========================================================== */
    if ("comment_delete".equals(action)) {
        int cmtId = Integer.parseInt(request.getParameter("comment_id"));
        List<Map<String,Object>> cmtList = commentsMap.get(postId);
        if (cmtList != null) {
            Iterator<Map<String,Object>> it = cmtList.iterator();
            while (it.hasNext()) {
                Map<String,Object> c = it.next();
                if ((int)c.get("id") == cmtId) {
                    it.remove();
                    break;
                }
            }
        }
        response.sendRedirect("index.jsp?mode=view&post_id=" + postId);
        return;
    }
%>

<%-- ==========================================================
     📋 9. 게시글 목록 화면
     ========================================================== --%>
<% if (mode == null) { %>
<h2>📋 게시글 목록</h2>
<table>
    <tr>
        <th>번호</th><th>제목</th><th>작성자</th><th>좋아요</th><th>조회수</th><th>작성일</th>
    </tr>
    <% if (posts.isEmpty()) { %>
        <tr><td colspan="6" style="color:red;">등록된 게시글이 없습니다.</td></tr>
    <% } else { 
        for (Map<String,Object> p : posts) { %>
        <tr>
            <td><%= p.get("id") %></td>
            <td><a href="index.jsp?mode=view&post_id=<%= p.get("id") %>"><%= p.get("title") %></a></td>
            <td><%= p.get("author") %></td>
            <td><%= p.get("like") %></td>
            <td><%= p.get("view") %></td>
            <td><%= p.get("created") %></td>
        </tr>
    <%  } } %>
</table>
<br>
<form method="get" action="index.jsp">
    <input type="hidden" name="mode" value="write">
    <input type="submit" value="📝 새 글 작성">
</form>

<%-- ==========================================================
     ✏️ 10. 게시글 작성 화면
     ========================================================== --%>
<% } else if ("write".equals(mode)) { %>
<h2>📝 게시글 작성</h2>
<form method="post" action="index.jsp">
    <input type="hidden" name="action" value="insert">
    <p>제목: <input type="text" name="title" required></p>
    <p>내용: <textarea name="content" rows="8" required></textarea></p>
    <p>작성자: <input type="text" name="author" required></p>
    <input type="submit" value="등록">
</form>
<button class="home-btn" onclick="location.href='index.jsp'">🏠 홈으로</button>

<%-- ==========================================================
     📖 11. 게시글 보기 화면
     ========================================================== --%>
<% } else if ("view".equals(mode)) { 
    Map<String,Object> post = null;
    for (Map<String,Object> p : posts) {
        if ((int)p.get("id") == postId) {
            post = p;
            p.put("view", (int)p.get("view") + 1);
            break;
        }
    }
    if (post == null) {
        out.println("<p style='color:red;'>게시글이 존재하지 않습니다.</p>");
    } else {
%>
<h2>📄 게시글 보기</h2>
<p><b>제목:</b> <%= post.get("title") %></p>
<p><b>작성자:</b> <%= post.get("author") %></p>
<p><b>조회수:</b> <%= post.get("view") %></p>
<p><b>좋아요:</b> <%= post.get("like") %></p>
<p><b>작성일:</b> <%= post.get("created") %></p>
<hr>
<pre><%= post.get("content") %></pre>
<hr>

<form method="post" action="index.jsp">
    <input type="hidden" name="action" value="like">
    <input type="hidden" name="post_id" value="<%= postId %>">
    <input type="submit" value="❤️ 좋아요">
</form>

<form method="get" action="index.jsp">
    <input type="hidden" name="mode" value="edit">
    <input type="hidden" name="post_id" value="<%= postId %>">
    <input type="submit" value="✏️ 수정">
</form>

<form method="post" action="index.jsp" onsubmit="return confirmDelete();">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="post_id" value="<%= postId %>">
    <input type="submit" value="🗑 삭제">
</form>

<button class="home-btn" onclick="location.href='index.jsp'">🏠 홈으로</button>

<hr>
<h3>💬 댓글 목록</h3>
<%
    List<Map<String,Object>> cmtList = commentsMap.get(postId);
    if (cmtList == null || cmtList.isEmpty()) {
        out.println("<p>아직 댓글이 없습니다 😿</p>");
    } else {
%>
<table>
    <tr><th>작성자</th><th>내용</th><th>작성일</th><th>삭제</th></tr>
    <% for (Map<String,Object> c : cmtList) { %>
        <tr>
            <td><%= c.get("writer") %></td>
            <td><%= c.get("content") %></td>
            <td><%= c.get("created") %></td>
            <td>
                <form method="post" action="index.jsp" style="display:inline;">
                    <input type="hidden" name="action" value="comment_delete">
                    <input type="hidden" name="post_id" value="<%= postId %>">
                    <input type="hidden" name="comment_id" value="<%= c.get("id") %>">
                    <input type="submit" value="삭제">
                </form>
            </td>
        </tr>
    <% } %>
</table>
<% } %>

<h3>✏️ 댓글 작성</h3>
<form method="post" action="index.jsp">
    <input type="hidden" name="action" value="comment_insert">
    <input type="hidden" name="post_id" value="<%= postId %>">
    작성자: <input type="text" name="writer" required><br><br>
    <textarea name="content" rows="3" placeholder="댓글을 입력하세요" required></textarea><br><br>
    <input type="submit" value="등록">
</form>

<% } %>

<%-- ==========================================================
     🧰 12. 게시글 수정 화면
     ========================================================== --%>
<% } else if ("edit".equals(mode)) { 
    Map<String,Object> post = null;
    for (Map<String,Object> p : posts) {
        if ((int)p.get("id") == postId) post = p;
    }
%>
<h2>🧰 게시글 수정</h2>
<form method="post" action="index.jsp">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="post_id" value="<%= postId %>">
    <p>제목: <input type="text" name="title" value="<%= post.get("title") %>"></p>
    <p>내용: <textarea name="content" rows="8"><%= post.get("content") %></textarea></p>
    <input type="submit" value="수정 완료">
</form>
<button class="home-btn" onclick="location.href='index.jsp'">🏠 홈으로</button>
<% } %>

</div>
</body>
</html>
