// 📦 패키지 경로 : com.pet.controller
//   👉 게시글 상세보기, 조회수 증가, 댓글, 좋아요 토글 기능을 담당하는 컨트롤러입니다.
package com.pet.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.pet.dao.PostDAO;
import com.pet.dto.PostDTO;

/**
 * 📄 PostViewController
 * ---------------------------------------------------------
 * 🧩 역할:
 *   1️⃣ 게시글 클릭 시 상세 내용을 보여줌
 *   2️⃣ 조회수 1 증가 (중복 방지)
 *   3️⃣ 댓글 목록 출력
 *   4️⃣ 좋아요 버튼 클릭 시 on/off 토글
 */
@WebServlet("/post/view.do")
public class PostViewController extends HttpServlet {

    /**
     * 🧠 GET 요청 — 게시글 상세보기
     * ---------------------------------------------------------
     * 💡 처리 순서:
     *   ① post_id 파라미터 확인
     *   ② DB 연결 후 게시글 정보 + 댓글 목록 조회
     *   ③ 조회수 1 증가
     *   ④ JSP로 데이터 전달 및 forward
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // ✅ 게시글 번호(post_id) 파라미터 가져오기
        String param = request.getParameter("post_id");
        if (param == null || param.isEmpty()) {
            // ⚠️ post_id가 없을 경우 목록으로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/post/list.do");
            return;
        }

        int postId = Integer.parseInt(param); // 문자열 → 정수 변환

        // ✅ 로그인 정보(세션) 가져오기
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // ✅ DB 접근을 위한 DAO 생성
        PostDAO dao = new PostDAO();

        PostDTO post = null;
        List<Map<String, Object>> comments = null;

        try {
            // 👁 조회수 기록 (중복 방지용 ownerId 포함)
            dao.insertView(postId, ownerId);

            // 📄 게시글 상세 조회 (작성자 닉네임, 좋아요 수 포함)
            post = dao.selectOne(postId);

            // 💬 댓글 목록 조회
            comments = dao.selectComments(postId);

        } catch (Exception e) {
            System.out.println("❌ 게시글 상세보기 중 오류 발생");
            e.printStackTrace();
        } finally {
            // ✅ 모든 DB 작업이 끝난 후 커넥션 반납
            dao.close();
        }

        // 🚫 게시글이 존재하지 않을 경우 목록으로 이동
        if (post == null) {
            response.sendRedirect(request.getContextPath() + "/post/list.do");
            return;
        }

        // ✅ JSP에 데이터 전달
        request.setAttribute("post", post);
        request.setAttribute("comments", comments);

        // ✅ JSP로 포워드 (화면 출력)
        request.getRequestDispatcher("/post/view.jsp").forward(request, response);
    }

    /**
     * ❤️ POST 요청 — 좋아요 토글 처리
     * ---------------------------------------------------------
     * 💡 처리 순서:
     *   ① post_id 파라미터 확인
     *   ② 세션에서 로그인 정보 가져오기
     *   ③ 좋아요 on/off 토글
     *   ④ 다시 상세보기 페이지로 이동
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 인코딩 처리
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // ✅ 세션에서 로그인 정보(ownerId) 가져오기
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // ✅ post_id 파라미터 확인 및 안전 처리
        String postParam = request.getParameter("post_id");
        if (postParam == null || postParam.trim().isEmpty()) {
            // ⚠️ post_id가 없을 경우 목록으로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/post/list.do");
            return;
        }

        int postId = Integer.parseInt(postParam);

        // 🚫 로그인하지 않은 사용자일 경우 (테스트용 임시 처리)
        /*
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }
        */

        // ✅ 테스트용: 로그인 안 되어 있으면 임시 ID 지정 (OWNER 테이블에 실제 존재하는 유저)
        if (ownerId == null) {
            ownerId = 1;
            session.setAttribute("ownerId", ownerId);
        }

        // ✅ DB 접근
        PostDAO dao = new PostDAO();

        try {
            // ❤️ 좋아요 토글 (on/off)
            dao.toggleLike(postId, ownerId);
        } catch (Exception e) {
            System.out.println("❌ 좋아요 토글 처리 실패");
            e.printStackTrace();
        } finally {
            // ✅ DB 자원 해제
            dao.close();
        }

        // ✅ 좋아요 처리 후 다시 상세보기로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + postId);
    }
}
