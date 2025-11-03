// 📦 이 파일은 com.pet.controller 폴더 안에 있어요.
//     → 웹 페이지의 요청을 받아서 “게시글 보기(view.jsp)” 화면으로 연결해주는 역할이에요.
package com.pet.controller;

import java.io.IOException;         // 💡 입출력 관련 에러 처리를 위해 필요
import java.util.List;             // 💡 여러 개의 데이터를 담을 때 사용 (예: 댓글 목록)
import java.util.Map;              // 💡 댓글 한 줄마다 key:value 쌍으로 데이터 저장

import jakarta.servlet.ServletException;        // 💡 서블릿에서 예외(오류) 처리용
import jakarta.servlet.annotation.WebServlet;   // 💡 URL 주소를 서블릿에 연결해주는 기능
import jakarta.servlet.http.*;                  // 💡 request, response, session 등 웹 기능

// 🧱 DAO(DB 작업 클래스)와 DTO(데이터 상자 클래스)를 불러옴
import com.pet.dao.PostDAO;
import com.pet.dto.PostDTO;

/**
 * 📄 PostViewController
 * ---------------------------------------------------------
 * 🧩 하는 일:
 *   1️⃣ 게시글을 클릭하면 상세 내용을 보여줌
 *   2️⃣ 조회수를 1 증가시킴 (중복 방지)
 *   3️⃣ 댓글 목록을 불러옴
 *   4️⃣ 좋아요 버튼을 누르면 on/off 토글함
 */
@WebServlet("/post/view.do") // 🌐 /post/view.do 주소로 요청이 오면 이 클래스가 실행돼요.
public class PostViewController extends HttpServlet {

    // 🧠 GET 요청 (주소창에서 들어오거나, 링크 클릭 시 실행)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글 깨짐 방지 (요청과 응답 둘 다)
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // ✅ 1️⃣ 게시글 번호(post_id) 가져오기
        String param = request.getParameter("post_id"); // URL에서 ?post_id=3 같은 값을 받음
        if (param == null || param.isEmpty()) { // 만약 없거나 비어있으면
            // 🚫 목록 페이지로 돌려보냄
            response.sendRedirect(request.getContextPath() + "/post/list.do");
            return;
        }
        int postId = Integer.parseInt(param); // 문자열을 숫자로 바꿔줌 (ex: "3" → 3)

        // ✅ 2️⃣ 세션에서 로그인한 사용자(ownerId) 가져오기
        HttpSession session = request.getSession(); // 세션 불러오기
        Integer ownerId = (Integer) session.getAttribute("ownerId"); // 로그인한 사람 번호

        // ✅ 3️⃣ DAO 생성 (DB 연결)
        PostDAO dao = new PostDAO();

        // ✅ 4️⃣ 조회수 증가 (단, 같은 사용자가 여러 번 봐도 1회만 올라가도록 처리)
        dao.insertView(postId, ownerId);

        // ✅ 5️⃣ 게시글 내용(DB에서 1개) 가져오기
        PostDTO post = dao.selectOne(postId);

        // ✅ 6️⃣ 댓글 목록 가져오기 (한 게시글에 달린 모든 댓글)
        List<Map<String, Object>> comments = dao.selectComments(postId);
        // 💡 Map은 key:value 구조라서, 예: {"nickname":"홍길동", "content":"귀여워요"} 식으로 담김
        request.setAttribute("comments", comments); // JSP에서 ${comments} 로 쓸 수 있음

        // 🚫 게시글이 없을 경우 목록으로 이동
        if (post == null) {
            dao.close();
            response.sendRedirect(request.getContextPath() + "/post/list.do");
            return;
        }

        // ✅ 7️⃣ 게시글 정보를 JSP로 넘김
        request.setAttribute("post", post);

        // 🔒 8️⃣ DB 연결 종료
        dao.close();

        // ✅ 9️⃣ /post/view.jsp 로 화면 넘기기 (forward)
        request.getRequestDispatcher("/post/view.jsp").forward(request, response);
    }

    /**
     * ❤️ 좋아요(Like) 버튼 처리
     * ---------------------------------------------------------
     * 💡 Ajax나 form으로 POST 요청이 오면 실행돼요.
     *     → 이미 좋아요를 눌렀다면 취소, 아니면 추가로 바꿔줌.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // ✅ 로그인 정보 확인
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // ✅ 어떤 게시글에 좋아요 눌렀는지 가져오기
        int postId = Integer.parseInt(request.getParameter("post_id"));

        // 🚫 로그인 안 되어 있으면 로그인 페이지로 이동
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // ✅ DB 연결 후 좋아요 토글
        PostDAO dao = new PostDAO();

        // 💖 toggleLike() : 이미 좋아요가 있으면 취소, 없으면 새로 추가
        boolean liked = dao.toggleLike(postId, ownerId);

        dao.close(); // 🔒 DB 연결 닫기

        // ✅ 결과 반영 후 다시 상세보기 페이지로 이동 (새로고침 느낌)
        response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + postId);
    }
}
