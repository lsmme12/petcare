// 📦 com.pet.controller 패키지 안에 있는 파일이에요.
//     👉 "Controller"는 웹 요청을 받아서 처리하고 다음 페이지로 넘겨주는 역할을 해요.
package com.pet.controller;

import java.io.File;  // 💾 파일 관련 기능 (폴더 만들기, 파일 저장 등)
import java.io.IOException; // 💡 입출력(파일, 네트워크) 에러 처리용
import jakarta.servlet.ServletException; // ⚙️ 서블릿에서 오류 발생 시 처리용
import jakarta.servlet.annotation.MultipartConfig; // 📎 파일 업로드용 설정 어노테이션
import jakarta.servlet.annotation.WebServlet; // 🌐 URL 주소를 서블릿과 연결
import jakarta.servlet.http.*; // 💬 HTTP 요청(request), 응답(response), 세션(session) 관련 기능

// 🧱 DB 처리용 클래스(PostDAO)와 데이터 저장용 상자(PostDTO)를 불러와요.
import com.pet.dao.PostDAO;
import com.pet.dto.PostDTO;

/**
 * 📄 PostWriteController
 * ----------------------------------------------------------
 * 💡 하는 일:
 *   1️⃣ 로그인한 사용자만 글을 쓸 수 있게 함
 *   2️⃣ 글 제목 + 내용 + 파일을 입력받음
 *   3️⃣ DB(Post 테이블)에 저장
 *   4️⃣ 저장 후 목록 페이지로 이동
 */
@WebServlet("/post/write.do") // 🌐 이 주소로 요청이 오면 이 클래스가 실행됨
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 💾 최대 업로드 용량 = 10MB
public class PostWriteController extends HttpServlet {

    // 🧠 GET 요청 (페이지 들어올 때 실행됨)
    //    예: “글쓰기 버튼”을 눌러서 /post/write.do 로 이동할 때
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 1️⃣ 로그인한 사용자인지 확인하기 위해 세션을 가져옴
        HttpSession session = request.getSession();

        // 👤 로그인한 사용자의 번호(ownerId)를 세션에서 꺼냄
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // 🚫 로그인이 안 되어 있으면 로그인 페이지로 보냄
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return; // ❌ 밑에 코드 더 이상 실행 안 함
        }

        // ✅ 2️⃣ 로그인 되어 있으면 글쓰기 폼 페이지로 이동
        request.getRequestDispatcher("/post/write.jsp").forward(request, response);
    }

    // 🧠 POST 요청 (폼에서 "작성 완료" 버튼을 눌렀을 때 실행됨)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 💬 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");

        // 🎒 로그인 세션 가져오기
        HttpSession session = request.getSession();

        // 👤 로그인한 사용자 번호(ownerId)
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // 🚫 로그인하지 않았다면 로그인 페이지로 보냄
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        // ✅ 1️⃣ 사용자가 입력한 제목(title), 내용(content)을 가져옴
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // ✅ 2️⃣ 파일 업로드 처리 시작
        // 💾 업로드된 파일을 저장할 폴더 경로 가져오기
        String saveDir = request.getServletContext().getRealPath("/uploads");

        // 📁 uploads 폴더가 없으면 새로 만들어줌
        File uploadDir = new File(saveDir);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // 📎 폼에서 파일 입력칸(name="file")에 들어온 파일 정보 가져오기
        Part filePart = request.getPart("file");

        // 📄 파일 관련 정보 저장용 변수
        String original = null;      // 사용자가 업로드한 원본 파일 이름
        String stored = null;        // 서버에 저장될 새 파일 이름
        String contentType = null;   // 파일의 종류 (예: image/png)
        long size = 0;               // 파일 크기 (byte 단위)

        // ✅ 파일이 실제로 업로드된 경우에만 처리
        if (filePart != null && filePart.getSize() > 0) {
            original = filePart.getSubmittedFileName(); // 📎 원본 파일명
            contentType = filePart.getContentType();    // 📎 파일 형식
            size = filePart.getSize();                  // 📎 파일 크기

            // 💡 확장자 구하기 (.jpg, .png 등)
            String ext = "";
            int dot = original.lastIndexOf(".");
            if (dot != -1) ext = original.substring(dot);

            // ⚙️ 저장될 파일명 만들기 (시간 + 해시코드로 이름 중복 방지)
            stored = System.currentTimeMillis() + "_" + Math.abs(original.hashCode()) + ext;

            // 💾 서버 폴더(/uploads)에 파일을 실제로 저장
            filePart.write(saveDir + File.separator + stored);
        }

        // ✅ 3️⃣ 사용자가 쓴 글과 파일 정보를 DTO에 담기
        PostDTO dto = new PostDTO();
        dto.setOwnerId(ownerId);                // 글쓴이 번호
        dto.setTitle(title);                    // 제목
        dto.setContent(content);                // 내용
        dto.setOriginalFilename(original);      // 원본 파일명
        dto.setStoredFilename(stored);          // 서버 저장 파일명
        dto.setContentType(contentType);        // 파일 타입
        dto.setFileSizeBytes(size);             // 파일 크기
        dto.setHasAttachment(stored != null ? "Y" : "N"); // 파일이 있으면 Y, 없으면 N

        // ✅ 4️⃣ DB에 저장 (DAO 호출)
        PostDAO dao = new PostDAO();
        dao.insertPost(dto); // 📦 DB의 POST 테이블에 새 글 추가
        dao.close(); // 🔒 DB 연결 닫기

        // ✅ 5️⃣ 작성 완료 후 목록 페이지로 이동
        response.sendRedirect(request.getContextPath() + "/post/list.do");
    }
}
