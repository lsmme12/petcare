package com.pet.controller;

/**
 * 반려동물 투약기록 관련 요청을 처리하는 서블릿입니다.
 *
 * 아래는 초보자도 이해하기 쉬운 설명입니다.
 * - 서블릿은 웹에서 들어오는 요청(HttpServletRequest)을 받아서 처리하고
 *   응답(HttpServletResponse)을 돌려주는 자바 클래스입니다.
 * - 이 클래스는 투약 기록을 "보여주기(list)", "등록하기(add)", "수정하기(edit)",
 *   "등록 처리(create)", "수정 처리(update)", "삭제 처리(delete)" 를 담당합니다.
 * - 화면 이동 방식
 *   1) forward: 서버 내부에서 다른 JSP를 실행해서 같은 요청 안에서 결과를 보여줌
 *   2) redirect: 브라우저에게 다른 URL로 다시 요청하라고 알려서 새 요청을 만듬
 */

import com.pet.dao.MediRecordDAO;
import com.pet.dao.PetDAO;
import com.pet.dto.MediRecordDTO;
import com.pet.dto.PetDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "MediRecordServlet", urlPatterns = {"/api/medi/*"})
public class MediRecordServlet extends HttpServlet {

    // GET 요청이 들어오면 이 메서드가 호출됩니다.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Debug log to help diagnose mapping/runtime issues
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        System.out.println("[MediRecordServlet] doGet invoked. servletPath=" + servletPath + " pathInfo=" + pathInfo + " query=" + req.getQueryString());

        // req.getPathInfo() 는 매핑된 URL 뒤의 추가 경로를 가져옵니다.
        // 예: /medi/list, /medi/add, /medi/edit 등
        String path = req.getPathInfo(); // /list, /add, /edit
        if (path == null || "/".equals(path) || path.startsWith("/list")) {
            // 목록 화면을 보여줍니다.
            list(req, resp);
        } else if (path.startsWith("/add")) {
            // 등록(입력) 화면으로 이동합니다.
            showAdd(req, resp);
        } else if (path.startsWith("/edit")) {
            // 수정 화면(폼)을 보여줍니다.
            showEdit(req, resp);
        } else {
            // 알 수 없는 경로면 404 에러를 보냅니다.
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // POST 요청이 들어오면 이 메서드가 호출됩니다.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[MediRecordServlet] doPost invoked. servletPath=" + req.getServletPath() + " pathInfo=" + req.getPathInfo());
        // HTML 폼에서 _method 파라미터를 붙이면 PUT/DELETE 흉내를 낼 수 있습니다.
        // 예: <input type="hidden" name="_method" value="PUT" />
        String method = req.getParameter("_method");
        if ("PUT".equalsIgnoreCase(method)) {
            // 수정 처리
            update(req, resp);
            return;
        } else if ("DELETE".equalsIgnoreCase(method)) {
            // 삭제 처리
            delete(req, resp);
            return;
        }
        // 기본은 등록 처리
        create(req, resp);
    }

    // 목록 조회: 특정 반려동물(petId)의 투약 기록을 보여줌
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // petId 파라미터가 없으면 0으로 처리
        int petId = parseInt(req.getParameter("petId"), 0);
        if (petId == 0) {
            // petId가 없으면 먼저 반려동물을 선택하게 합니다.
            // 소유자(ownerId)를 기준으로 반려동물 목록(select 박스)을 보여주는 JSP로 이동
            // 실제 서비스에서는 ownerId를 로그인 세션에서 가져옵니다.
            int ownerId = parseInt(req.getParameter("ownerId"), 1);
            PetDAO pdao = new PetDAO();
            List<PetDTO> pets = pdao.getPetsByOwner(ownerId); // DB에서 반려동물 목록을 가져옴
            req.setAttribute("pets", pets); // JSP에 전달할 데이터 세팅
            RequestDispatcher rd = req.getRequestDispatcher("/medi/medilist.jsp");
            // forward 사용: 브라우저 주소는 바뀌지 않고 JSP가 실행됩니다.
            rd.forward(req, resp);
            return;
        }

        // petId가 있으면 해당 반려동물의 투약기록을 DB에서 가져와서 보여줍니다.
        MediRecordDAO dao = new MediRecordDAO();
        List<MediRecordDTO> list = dao.list(petId); // DB 조회
        req.setAttribute("petId", petId);
        req.setAttribute("list", list); // JSP에 전달
        // Forward to JSP view (public path)
        RequestDispatcher rd = req.getRequestDispatcher("/medi/medilist.jsp");
        rd.forward(req, resp);
    }

    // 등록 화면으로 이동 (폼 보여주기)
    private void showAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 화면에서 petId를 hidden으로 유지하거나 쿼리스트링에 포함시킵니다.
        int petId = parseInt(req.getParameter("petId"), 0);
        req.setAttribute("petId", petId);
        // Forward to existing JSP add form
        RequestDispatcher rd = req.getRequestDispatcher("/medi/mediadd.jsp");
        rd.forward(req, resp);
    }

    // 수정 화면으로 이동 (기본적으로 폼에 기존 값을 채워 넣는 역할)
    private void showEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 실제로는 DB에서 기존 레코드를 조회해서 값을 채워 넣는 것이 일반적입니다.
        int recordId = parseInt(req.getParameter("recordId"), 0);
        int petId = parseInt(req.getParameter("petId"), 0);
        // 파라미터에서 전달된 값을 받아서 폼에 보여주기 위해 세팅
        String medicine = nvl(req.getParameter("medicine"));
        String dosage = nvl(req.getParameter("dosage")); // 예: "yyyy-MM-dd HH:mm:ss"
        if (!dosage.isEmpty()) dosage = dosage.replace(' ', 'T'); // datetime-local 입력값 포맷으로 변경
        req.setAttribute("recordId", recordId);
        req.setAttribute("petId", petId);
        req.setAttribute("medicine", medicine);
        req.setAttribute("dosage", dosage);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/medi/edit.jsp");
        rd.forward(req, resp);
    }

    // 등록 처리: 폼에서 받은 값으로 DB에 저장
    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int petId = parseInt(req.getParameter("petId"), 0);
        String medicine = nvl(req.getParameter("medicine"));
        String dosageTime = nvl(req.getParameter("dosageTime"));
        String msg;
        try {
            // input[type=datetime-local] 값(예: 2025-10-31T09:30)을 LocalDateTime으로 파싱
            LocalDateTime ldt = LocalDateTime.parse(dosageTime);
            Date utilDate = new Date(Timestamp.valueOf(ldt).getTime());
            MediRecordDTO dto = new MediRecordDTO();
            dto.setMedicine(medicine);
            dto.setDosageTime(utilDate);
            MediRecordDAO dao = new MediRecordDAO();
            int r = dao.insert(dto, petId); // DB에 저장
            msg = (r>0) ? "등록되었습니다." : "등록 실패(변경 없음).";
        } catch (Exception e) {
            // 오류가 나면 메시지를 세션에 남긴 뒤 목록으로 이동
            msg = "등록 오류: " + e.getMessage();
        }
        req.getSession().setAttribute("flash", msg); // 한 번만 보여줄 알림 메시지 저장
        // redirect 사용: 브라우저에게 목록 페이지를 새로 요청하라고 보냄
        resp.sendRedirect(req.getContextPath() + "/api/medi/list?petId=" + petId);
    }

    // 수정 처리: 폼에서 받은 값으로 DB 업데이트
    private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        int recordId = parseInt(req.getParameter("recordId"), 0);
        String medicine = nvl(req.getParameter("medicine"));
        String dosageTime = nvl(req.getParameter("dosageTime"));
        String msg;
        try {
            LocalDateTime ldt = LocalDateTime.parse(dosageTime);
            Date utilDate = new Date(Timestamp.valueOf(ldt).getTime());
            MediRecordDTO dto = new MediRecordDTO();
            dto.setRecordId(recordId);
            dto.setMedicine(medicine);
            dto.setDosageTime(utilDate);
            MediRecordDAO dao = new MediRecordDAO();
            int r = dao.update(dto); // DB 업데이트 실행
            msg = (r>0) ? "수정되었습니다." : "수정 실패(변경 없음).";
        } catch (Exception e) {
            msg = "수정 오류: " + e.getMessage();
        }
        req.getSession().setAttribute("flash", msg);
        resp.sendRedirect(req.getContextPath() + "/api/medi/list?petId=" + petId);
    }

    // 삭제 처리: DB에서 해당 레코드 삭제
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        int recordId = parseInt(req.getParameter("recordId"), 0);
        String msg;
        try {
            MediRecordDAO dao = new MediRecordDAO();
            int r = dao.delete(recordId);
            msg = (r>0) ? "삭제되었습니다." : "삭제 실패(대상 없음).";
        } catch (Exception e) {
            msg = "삭제 오류: " + e.getMessage();
        }
        req.getSession().setAttribute("flash", msg);
        resp.sendRedirect(req.getContextPath() + "/api/medi/list?petId=" + petId);
    }

    // 문자열을 정수로 바꿀 때 사용하는 도우미 메서드
    private int parseInt(String s, int def) {
        // 숫자가 아니면 기본값(def)을 반환합니다.
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
    // null을 빈 문자열로 바꿔주는 단순한 도우미(널 세이프하게 사용)
    private String nvl(String s) { return s==null? "": s.trim(); }
}
