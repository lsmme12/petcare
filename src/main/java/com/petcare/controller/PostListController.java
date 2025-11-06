// 📦 이 클래스는 com.pet.controller 폴더 안에 있어요.
//     "컨트롤러"는 웹에서 오는 요청을 받아서 어떤 페이지로 보낼지 정해주는 역할이에요.
package com.petcare.controller;

import java.io.IOException;  // 🔹 입출력 에러(파일, 네트워크 등)를 처리할 때 필요
import java.util.List;       // 🔹 여러 개의 데이터를 한꺼번에 담는 리스트(List)를 쓰기 위해 필요

import jakarta.servlet.ServletException;        // 🔹 서블릿에서 예외(오류) 처리를 위해 필요
import jakarta.servlet.annotation.WebServlet;   // 🔹 URL 주소를 서블릿에 연결해주는 도구
import jakarta.servlet.http.*;                  // 🔹 요청(request), 응답(response) 관련 기능

// 🧱 DB와 대화하는 클래스(DAO)와 데이터를 담는 클래스(DTO)를 불러옴
import com.petcare.dao.PostDAO;
import com.petcare.dto.PostDTO;

/**
 * 📄 PostListController
 * --------------------------------------------------------
 * 💡 이 클래스는 게시글 목록을 보여주는 페이지를 담당해요.
 * 
 * 🧩 하는 일:
 *   1️⃣ 데이터베이스(DB)에서 게시글들을 전부 가져오기
 *   2️⃣ JSP(list.jsp)에게 그 목록을 넘겨주기
 *   3️⃣ 삭제되지 않은 글만 보여주기 (is_deleted = 'N')
 */
@WebServlet("/post/list.do") // 🌐 주소창에 /post/list.do로 들어오면 이 클래스가 실행돼요.
public class PostListController extends HttpServlet {

    // 🧠 GET 방식 요청이 들어올 때 실행되는 메서드예요.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글이 깨지지 않도록 문자 인코딩을 UTF-8로 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // ✅ 1️⃣ DAO 객체를 만들어서 DB에 있는 게시글 목록을 가져오기
        PostDAO dao = new PostDAO();            // 🧩 DB와 연결할 준비를 함
        int page = 1;        // 현재 페이지 (기본값 1)
        int limit = 10;      // 한 페이지당 게시글 10개

        String p = request.getParameter("page");
        if (p != null && p.matches("\\d+")) {
            page = Integer.parseInt(p);
        }

        // ✅ 페이징 데이터 조회
        int totalCount = dao.countPosts();  // 총 게시글 수
        int totalPages = (int) Math.ceil(totalCount / (double) limit);

        // ✅ 해당 페이지에 맞는 10개 게시글 가져오기
        List<PostDTO> postList = dao.selectPaged(page, limit);

        dao.close();

        // ✅ JSP에서 사용할 데이터 전달
        request.setAttribute("postList", postList);
        request.setAttribute("page", page);
        request.setAttribute("limit", limit);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPages", totalPages);

        // ✅ list.jsp로 이동
        request.getRequestDispatcher("/post/list.jsp").forward(request, response);
    }

    // 🧠 POST 요청이 들어와도 doGet()과 같은 기능을 하도록 설정
    // (예: 검색 폼이나 버튼에서 POST 방식으로 요청했을 때도 처리 가능)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 👉 doPost()가 호출되면 doGet()을 그대로 실행함.
        doGet(request, response);
    }
}
