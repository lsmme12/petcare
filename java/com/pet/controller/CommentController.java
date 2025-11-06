// 📦 이 파일은 "댓글(Comment)" 기능을 처리하는 컨트롤러야.
//     사용자가 댓글을 달거나 지울 때 작동하는 Java 클래스야.
//     JSP에서 /comment.do 로 요청이 들어오면 여기서 처리함.

package com.pet.controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.pet.dao.CommentDAO;
import com.pet.dto.CommentDTO;

// 🪄 @WebServlet은 "이 클래스는 /comment.do 주소로 오는 요청을 처리한다"는 뜻이야.
@WebServlet("/comment.do")
public class CommentController extends HttpServlet {

    // 🧠 doPost()는 "POST 방식"으로 요청이 올 때 실행되는 함수야.
    // 예를 들어, 댓글 작성 폼에서 <form method="post"> 로 보낼 때 실행돼.
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 🕹️ JSP에서 보낸 'action' 파라미터를 가져옴 (예: add 또는 delete)
        String action = request.getParameter("action");

        // 🧱 댓글 관련 DB 작업을 위한 DAO 객체 생성
        CommentDAO dao = new CommentDAO();

        try {
            // ✅ 댓글 추가하기 (add)
            if ("add".equals(action)) {

                // 🎒 현재 로그인한 사용자의 정보를 가져오기 위해 세션을 불러옴
                HttpSession session = request.getSession();

                // 👤 로그인한 사용자의 번호(ownerId)를 세션에서 꺼냄
                Integer ownerId = (Integer) session.getAttribute("ownerId");

                // 🚫 [실제 서비스용 코드] — 로그인 안 되어 있으면 로그인 페이지로 이동
                /*
                if (ownerId == null) {
                    // 👉 로그인 페이지로 강제로 이동시킴.
                    response.sendRedirect(request.getContextPath() + "/member/login.jsp");
                    return; // 이 아래 코드는 실행하지 않고 끝냄.
                }
                */

                // 🧪 [테스트용 코드] — 로그인 안 되어 있으면 임시 계정으로 대체
                if (ownerId == null) {
                    ownerId = 1; // ✅ OWNER 테이블에 존재하는 테스트 계정
                    session.setAttribute("ownerId", ownerId);
                    System.out.println("⚠️ [테스트모드] 로그인 없이 댓글 작성 → 임시 ownerId=1 사용");
                }

                // 📝 새 댓글 정보를 담을 상자(CommentDTO)를 하나 만듦
                CommentDTO dto = new CommentDTO();

                // ✍️ 어떤 게시글(post_id)에 댓글을 다는 건지 가져옴
                dto.setPostId(Integer.parseInt(request.getParameter("post_id")));

                // 👤 댓글 작성자 번호
                dto.setOwnerId(ownerId);

                // 💬 댓글 내용
                dto.setContent(request.getParameter("content"));

                // 🧾 DB에 댓글 등록
                dao.insertComment(dto);

                // 🚀 댓글 작성 후 다시 해당 게시글 보기 페이지로 이동
                response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + dto.getPostId());

            // ❌ 댓글 삭제하기 (delete)
            } else if ("delete".equals(action)) {

                // 🆔 삭제할 댓글 번호 가져오기
                int commentId = Integer.parseInt(request.getParameter("comment_id"));

                // 📝 삭제 후 돌아갈 게시글 번호
                int postId = Integer.parseInt(request.getParameter("post_id"));

                // 🧹 DAO를 이용해서 DB에서 댓글 삭제
                dao.deleteComment(commentId);

                // 🚀 댓글 삭제 후 다시 게시글 보기 페이지로 이동
                response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + postId);
            }

        } catch (Exception e) {
            System.out.println("❌ 댓글 처리 중 오류 발생");
            e.printStackTrace();

        } finally {
            // 🔒 사용이 끝난 DAO(DB 연결)를 닫아줌 (자원 정리 필수)
            dao.close();
        }
    }
}
