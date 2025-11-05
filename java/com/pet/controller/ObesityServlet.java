package com.pet.controller;

import java.io.IOException;
import java.util.List;

import com.pet.dao.ObesityDAO;
import com.pet.dto.ObesityDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name="ObesityServlet", urlPatterns={"/obesity/*"})
public class ObesityServlet extends HttpServlet {

    /**
     * 초보자용 설명:
     * - 이 서블릿은 반려동물의 비만도 관련 요청을 처리합니다.
     * - 주요 기능: 목록 보기(list), 최신 항목 보기(latest), 새 항목 추가(add)
     * - URL 예: /obesity/list?petId=1, /obesity/latest?petId=1, /obesity/add
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || "/list".equals(path)) {
            // 목록 화면을 보여줍니다.
            list(req, resp);
        } else if ("/latest".equals(path)) {
            // 가장 최근에 저장된 비만도 정보를 보여줍니다.
            latest(req, resp);
        } else if ("/add".equals(path)) {
            // 추가 입력 화면으로 이동 (JSP로 forward)
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/obesity/add.jsp");
            rd.forward(req, resp);
        } else {
            // 정의되지 않은 경로는 404
            resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 이 서블릿은 단순히 POST로만 새 기록을 추가합니다.
        String method = req.getParameter("_method");
        if ("POST".equalsIgnoreCase(req.getMethod()) && (method == null || method.isEmpty())) {
            // 실제로는 form에서 보낸 값으로 DB에 저장하는 동작
            create(req, resp);
        } else {
            // 다른 메서드는 지원하지 않음
            resp.sendError(405);
        }
    }

    /**
     * 새 비만도 기록을 DB에 저장합니다.
     * 학생 가이드:
     * - 입력값: petId(int), check_1~check_5("Y"/"N")
     * - DAO.insert(dto)로 저장 후 flash 메시지 설정 → 목록으로 리다이렉트
     */
    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // form 체크박스 값은 체크되면 값이 있고, 없으면 null이 옵니다.
        int petId = parseInt(req.getParameter("petId"), 0);
        String c1 = nz(req.getParameter("check_1"), "N");
        String c2 = nz(req.getParameter("check_2"), "N");
        String c3 = nz(req.getParameter("check_3"), "N");
        String c4 = nz(req.getParameter("check_4"), "N");
        String c5 = nz(req.getParameter("check_5"), "N");

        String msg;
        try {
            ObesityDTO dto = new ObesityDTO();
            dto.setPetId(petId);
            dto.setCheck1(c1);
            dto.setCheck2(c2);
            dto.setCheck3(c3);
            dto.setCheck4(c4);
            dto.setCheck5(c5);

            ObesityDAO dao = new ObesityDAO();
            int r = dao.insert(dto);
            msg = (r>0) ? "비만도 기록이 저장되었습니다." : "저장 실패";
        } catch (Exception e) {
            msg = "저장 오류: " + e.getMessage();
        }
        // flash 메시지를 세션에 넣고 목록 페이지로 이동
        req.getSession().setAttribute("flash", msg);
        resp.sendRedirect(req.getContextPath() + "/obesity/list?petId=" + petId);
    }

    /**
     * 특정 반려동물의 비만도 기록 목록을 JSP로 전달합니다.
     * 학생 가이드:
     * - 입력값: petId(int), limit(옵션, 기본 20)
     * - request.setAttribute("rows", List<ObesityDTO>)로 JSP에 데이터 전달
     * - View: /WEB-INF/views/obesity/list.jsp
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        int limit = parseInt(req.getParameter("limit"), 20);
        ObesityDAO dao = new ObesityDAO();
        List<ObesityDTO> rows = dao.listByPet(petId, limit);
        req.setAttribute("rows", rows);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/obesity/list.jsp");
        rd.forward(req, resp);
    }

    /**
     * 특정 반려동물의 최신 비만도 기록 1건을 JSP로 전달합니다.
     * 학생 가이드:
     * - 입력값: petId(int)
     * - request.setAttribute("row", ObesityDTO)
     * - View: /WEB-INF/views/obesity/latest.jsp
     */
    private void latest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        ObesityDAO dao = new ObesityDAO();
        ObesityDTO row = dao.latestByPet(petId);
        req.setAttribute("row", row);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/obesity/latest.jsp");
        rd.forward(req, resp);
    }

    // 숫자로 변환하는 도우미 (예외 발생하면 기본값 반환)
    private int parseInt(String s, int def) { try { return Integer.parseInt(s); } catch(Exception e){ return def; } }
    // null 또는 빈값이면 기본값(d) 반환
    private String nz(String s, String d) { return (s==null || s.trim().isEmpty()) ? d : s.trim(); }
}
