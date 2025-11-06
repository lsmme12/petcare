package com.petcare.controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.petcare.dao.PostDAO;

/**
 * 📄 PostDeleteController
 * - 게시글 삭제 (is_deleted='Y'로 논리삭제)
 */
@WebServlet("/post/delete.do")
public class PostDeleteController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🧠 게시글 번호 가져오기
        int postId = Integer.parseInt(request.getParameter("post_id"));

        // 🗑 삭제 실행
        PostDAO dao = new PostDAO();
        int result = dao.deletePost(postId);
        dao.close();

        // ✅ 성공 시 목록으로 이동
        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/post/list.do");
        } else {
            response.getWriter().write("<script>alert('삭제 실패');history.back();</script>");
        }
    }
}
