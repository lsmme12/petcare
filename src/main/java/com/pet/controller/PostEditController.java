// 📦 이 클래스는 com.pet.controller 폴더(패키지)에 속해 있어요.
//     "컨트롤러"는 웹 요청을 받아서 처리하고, 다음 페이지로 이동시키는 역할을 해요.
package com.pet.controller;

import java.io.*; // 🔹 파일을 다루기 위한 클래스들 (File, IOException 등)
import jakarta.servlet.*; // 🔹 서블릿의 기본 기능을 사용하기 위해 필요해요.
import jakarta.servlet.annotation.MultipartConfig; // 🔹 파일 업로드(form-data)를 처리하기 위한 설정
import jakarta.servlet.annotation.WebServlet; // 🔹 URL 주소와 서블릿을 연결하기 위한 도구
import jakarta.servlet.http.*; // 🔹 요청(request), 응답(response), 세션(session) 관련 기능

// 🧱 DAO(데이터베이스 작업 담당)와 DTO(데이터 담는 상자)를 가져옴.
import com.pet.dao.PostDAO;
import com.pet.dto.PostDTO;

/**
 * 📄 PostEditController
 * -------------------------------------------------------
 * 🧩 하는 일:
 *   - 게시글 수정하거나 삭제할 때 동작하는 컨트롤러예요.
 *   - 파일 첨부가 있으면 교체, 없으면 그대로 유지하도록 처리돼요.
 */
@WebServlet("/post/edit.do") // 🌐 "/post/edit.do" 주소로 들어오면 이 클래스가 처리함.
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 💾 업로드 가능한 파일 최대 크기 = 10MB
public class PostEditController extends HttpServlet {

    /** ✅ 수정 페이지로 이동 (GET 요청 시 실행) */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 요청과 응답에서 한글이 깨지지 않게 인코딩을 UTF-8로 지정해요.
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 🆔 수정할 게시글의 번호(post_id)를 가져와요.
        int postId = Integer.parseInt(request.getParameter("post_id"));

        // 🧩 DB에서 게시글 정보를 가져오기 위해 DAO를 만들어요.
        PostDAO dao = new PostDAO();
        PostDTO post = dao.selectOne(postId); // 📋 해당 번호의 게시글 1개를 가져옴.
        dao.close(); // 🔒 DB 연결 닫기

        // 🚫 만약 게시글이 없으면 목록 페이지로 돌려보내요.
        if (post == null) {
            response.sendRedirect(request.getContextPath() + "/post/list.do");
            return;
        }

        // 📝 게시글 정보를 request에 담아서 edit.jsp에 전달해요.
        request.setAttribute("post", post);

        // 🖥️ edit.jsp로 이동 (게시글 수정 화면 보여줌)
        request.getRequestDispatcher("/post/edit.jsp").forward(request, response);
    }

    /** ✅ 수정 / 삭제 처리 (POST 요청 시 실행) */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 요청 데이터(제목, 내용 등)에서 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");

        // 🎒 현재 로그인한 사람 정보(세션) 가져오기
        HttpSession session = request.getSession();

        // 👤 세션에 저장된 ownerId (로그인한 사용자 번호)를 가져옴
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // 🚫 로그인 안 되어 있으면 로그인 페이지로 보냄
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // 🕹️ 어떤 작업인지 구분 (update = 수정, delete = 삭제)
        String action = request.getParameter("action");

        // 📋 대상 게시글 번호(post_id) 가져오기
        int postId = Integer.parseInt(request.getParameter("post_id"));

        // 🧱 DB 작업용 DAO 객체 생성
        PostDAO dao = new PostDAO();

        /* ===============================
         * 🛠 게시글 수정 기능
         * =============================== */
        if ("update".equals(action)) {

            // ✍️ 사용자가 수정한 제목과 내용을 가져옴
            String title = request.getParameter("title");
            String content = request.getParameter("content");

            // ✅ 업로드할 파일이 저장될 폴더 위치 설정
            String saveDir = request.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(saveDir);

            // 📁 uploads 폴더가 없으면 새로 만들기
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // 📎 파일 입력칸(name="file")의 실제 파일 정보를 가져옴
            Part filePart = request.getPart("file");

            // 📄 파일 관련 변수 준비
            String original = null;      // 사용자가 올린 파일의 원래 이름
            String stored = null;        // 서버에 저장될 파일명 (중복 방지를 위해 변경)
            String contentType = null;   // 파일 종류 (예: image/png)
            long size = 0;               // 파일 크기 (바이트 단위)

            // ✅ 파일이 실제로 업로드되었는지 확인
            if (filePart != null && filePart.getSize() > 0) {
                original = filePart.getSubmittedFileName(); // 원본 파일 이름
                contentType = filePart.getContentType();    // 파일 타입
                size = filePart.getSize();                  // 파일 크기

                // 📌 확장자 추출 (.jpg, .png 등)
                String ext = "";
                int dot = original.lastIndexOf(".");
                if (dot != -1) ext = original.substring(dot);

                // 🧩 저장용 파일 이름 만들기 (중복 방지)
                stored = System.currentTimeMillis() + "_" + Math.abs(original.hashCode()) + ext;

                // 💾 서버의 /uploads 폴더에 파일 저장
                filePart.write(saveDir + File.separator + stored);
            }

            // ✅ 게시글 정보를 DTO(데이터 상자)에 담기
            PostDTO dto = new PostDTO();
            dto.setPostId(postId);                  // 수정할 게시글 번호
            dto.setTitle(title);                    // 수정된 제목
            dto.setContent(content);                // 수정된 내용
            dto.setOriginalFilename(original);      // 원본 파일 이름
            dto.setStoredFilename(stored);          // 저장된 파일 이름
            dto.setContentType(contentType);        // 파일 타입
            dto.setFileSizeBytes(size);             // 파일 크기
            dto.setHasAttachment(stored != null ? "Y" : "N"); // 파일이 있으면 Y, 없으면 N

            // ✅ DB에서 게시글 내용 업데이트 실행
            dao.updatePost(postId, title, content);
            dao.close(); // 🔒 DB 연결 닫기

            // 🚀 수정이 끝나면 다시 게시글 보기 페이지로 이동
            response.sendRedirect(request.getContextPath() + "/post/view.do?post_id=" + postId);
        }

        /* ===============================
         * 🗑 게시글 삭제 기능
         * =============================== */
        else if ("delete".equals(action)) {

            // 🧹 해당 게시글을 DB에서 삭제
            dao.deletePost(postId);
            dao.close(); // 🔒 DB 연결 닫기

            // 🚀 삭제 완료 후 게시글 목록으로 이동
            response.sendRedirect(request.getContextPath() + "/post/list.do");
        }
    }
}
