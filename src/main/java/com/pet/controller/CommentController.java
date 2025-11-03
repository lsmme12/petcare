// 📦 이 파일은 "댓글(Comment)" 기능을 처리하는 컨트롤러야.
//     사용자가 댓글을 달거나 지울 때 작동하는 Java 클래스야.
//     JSP에서 /comment.do 로 요청이 들어오면 여기서 처리함.

package com.pet.controller; // 🔹 이 파일이 속한 폴더(패키지) 위치를 알려줌.

import java.io.IOException; // 🔹 입출력(파일, 네트워크 등) 관련 에러 처리를 위해 필요.
import jakarta.servlet.*; // 🔹 서블릿 관련 기본 기능을 쓰기 위해 불러옴.
import jakarta.servlet.annotation.WebServlet; // 🔹 URL 주소와 서블릿을 연결할 때 사용하는 어노테이션.
import jakarta.servlet.http.*; // 🔹 HttpServletRequest, HttpServletResponse, HttpSession 같은 기능을 쓰기 위해.

// 🧩 DAO(데이터베이스 접근용 클래스)와 DTO(데이터 저장용 클래스)를 불러옴.
import com.pet.dao.CommentDAO;
import com.pet.dto.CommentDTO;

// 🪄 @WebServlet은 "이 클래스는 /comment.do 주소로 오는 요청을 처리한다"는 뜻이야.
@WebServlet("/comment.do")
public class CommentController extends HttpServlet { // 🔹 HttpServlet을 상속해서 웹 요청을 처리하는 클래스야.

    // 🧠 doPost()는 "POST 방식"으로 요청이 올 때 실행되는 함수야.
    // 예를 들어, 댓글 작성 폼에서 <form method="post"> 로 보낼 때 실행돼.
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글이 깨지지 않게 문자 인코딩을 UTF-8로 설정해 줌.
        request.setCharacterEncoding("UTF-8");

        // 🕹️ JSP에서 보낸 'action' 파라미터를 가져옴 (예: add 또는 delete)
        String action = request.getParameter("action");

        // 🧱 데이터베이스와 연결해서 댓글 관련 작업을 하는 DAO 객체를 만듦.
        CommentDAO dao = new CommentDAO();

        try {
            // ✅ 댓글 추가하기 (add)
            if ("add".equals(action)) { // 만약 action 값이 "add"라면 아래 코드 실행.

                // 🎒 현재 로그인한 사용자의 정보를 가져오기 위해 세션을 불러옴.
                HttpSession session = request.getSession();

                // 👤 로그인한 사용자의 번호(ownerId)를 세션에서 꺼냄.
                Integer ownerId = (Integer) session.getAttribute("ownerId");

                // 🚫 만약 로그인을 안 했다면 (ownerId가 없으면)
                if (ownerId == null) {
                    // 👉 로그인 페이지로 강제로 이동시킴.
                    response.sendRedirect(request.getContextPath() + "/member/login.jsp");
                    return; // 이 아래 코드는 실행하지 않고 끝냄.
                }

                // 📝 새 댓글 정보를 담을 상자(CommentDTO)를 하나 만듦.
                CommentDTO dto = new CommentDTO();

                // ✍️ 어떤 게시글(post_id)에 댓글을 다는 건지 가져옴.
                dto.setPostId(Integer.parseInt(request.getParameter("post_id")));

                // 👤 댓글을 작성한 사람의 번호도 담아줌.
                dto.setOwnerId(ownerId);

                // 💬 댓글 내용(content)도 담아줌.
                dto.setContent(request.getParameter("content"));

                // 🧾 DAO를 이용해 DB에 댓글을 추가함.
                dao.insertComment(dto);

                // 🚀 댓글을 작성한 후, 다시 해당 게시글 보기 페이지로 이동시킴.
                //    (댓글을 단 게시글 번호를 URL에 붙여서 보내줌)
                response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + dto.getPostId());

            // ❌ 댓글 삭제하기 (delete)
            } else if ("delete".equals(action)) { // 만약 action 값이 "delete"라면

                // 🆔 삭제할 댓글 번호를 가져옴.
                int commentId = Integer.parseInt(request.getParameter("comment_id"));

                // 📝 삭제 후 다시 돌아갈 게시글 번호를 가져옴.
                int postId = Integer.parseInt(request.getParameter("post_id"));

                // 🧹 DAO를 이용해서 DB에서 댓글을 삭제함.
                dao.deleteComment(commentId);

                // 🚀 댓글 삭제가 끝나면 다시 게시글 보기 페이지로 이동함.
                response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + postId);
            }

        } finally {
            // 🔒 사용이 끝난 DAO(DB 연결)를 닫아줌. (자원 정리!)
            dao.close();
        }
    }
}
