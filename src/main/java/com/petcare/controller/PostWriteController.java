package com.petcare.controller;

import java.io.File;
import java.io.IOException;

import com.petcare.dao.PostDAO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PostDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/post/write.do")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024)
public class PostWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = resolveOwnerId(session);
        session.setAttribute("ownerId", ownerId);
        request.getRequestDispatcher("/post/write.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer ownerId = resolveOwnerId(session);
        session.setAttribute("ownerId", ownerId);

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        System.out.println("[PostWrite] title=" + title + ", ownerId=" + ownerId);

        String saveDir = request.getServletContext().getRealPath("/uploads");
        if (saveDir == null) {
            saveDir = System.getProperty("java.io.tmpdir") + File.separator + "petcare-uploads";
        }
        File uploadDir = new File(saveDir);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            System.err.println("[PostWrite] Failed to create upload directory: " + uploadDir.getAbsolutePath());
        }

        Part filePart = request.getPart("file");
        String original = null;
        String stored = null;
        String contentType = null;
        long size = 0L;

        if (filePart != null && filePart.getSize() > 0) {
            original = filePart.getSubmittedFileName();
            contentType = filePart.getContentType();
            size = filePart.getSize();

            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot != -1) {
                ext = original.substring(dot);
            }
            stored = System.currentTimeMillis() + "_" + Math.abs(original.hashCode()) + ext;
            filePart.write(new File(uploadDir, stored).getAbsolutePath());
            System.out.println("[PostWrite] uploaded file=" + stored);
        }

        PostDTO dto = new PostDTO();
        dto.setOwnerId(ownerId);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setOriginalFilename(original);
        dto.setStoredFilename(stored);
        dto.setContentType(contentType);
        dto.setFileSizeBytes(size);
        dto.setHasAttachment(stored != null ? "Y" : "N");

        PostDAO dao = new PostDAO();
        int result = 0;
        try {
            result = dao.insertPost(dto);
        } finally {
            dao.close();
        }

        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/post/list.do");
        } else {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<h3>게시글 등록에 실패했습니다. 잠시 후 다시 시도해 주세요.</h3>");
        }
    }

    private Integer resolveOwnerId(HttpSession session) {
        Integer ownerId = (Integer) session.getAttribute("ownerId");
        if (ownerId != null && ownerId > 0) {
            return ownerId;
        }
        OwnerVO loginUser = (OwnerVO) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getOwnerId() > 0) {
            return loginUser.getOwnerId();
        }
        // 개발 편의를 위한 기본값 (운영에서는 로그인 강제 필요)
        return 1;
    }
}
