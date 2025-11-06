package com.petcare.controller;

import java.io.IOException;
import java.util.Collections;

import com.petcare.dao.ExerRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.ExerRecordDTO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/exer/insert.do")
public class ExerInsertServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ExerRecordDAO dao = new ExerRecordDAO();
    private PetDAO petDao = new PetDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        try {
            int petId = parseInt(req.getParameter("petId"), 0);
            String exerDateStr = req.getParameter("exer_date");
            String exerTimeStr = req.getParameter("exer_time");
            String exerLevel = req.getParameter("exer_level");
            String memo = req.getParameter("memo");

            if (petId <= 0 || exerDateStr == null || exerTimeStr == null ||
                exerDateStr.isEmpty() || exerTimeStr.isEmpty()) {
                throw new ServletException("필수 입력값이 누락되었습니다.");
            }

            // Pet 소유자 확인
            PetDTO pet = petDao.getPetById(petId);
            if (pet == null || pet.getOwnerId() != ownerId) {
                req.getSession().setAttribute("flash", "권한이 없는 반려동물 접근입니다.");
                resp.sendRedirect(req.getContextPath() + "/pet/list.do");
                return;
            }

         // HTML form에서 받은 문자열을 java.sql.Date로 변환
            java.sql.Date exerDate = java.sql.Date.valueOf(exerDateStr); 
            int exerTime = parseInt(exerTimeStr, 0);

            ExerRecordDTO record = new ExerRecordDTO();
            record.setPetId(petId);
            record.setExerDate(exerDate);
            record.setExerTime(exerTime);
            record.setExerLevel(exerLevel != null ? exerLevel : "");
            record.setMemo(memo != null ? memo : "");

            int[] result = dao.insertRecordsBatch(Collections.singletonList(record));

            String msg = (result.length > 0 && result[0] > 0) ? "운동 기록이 등록되었습니다." : "등록 실패: DB insert 오류";
            req.getSession().setAttribute("flash", msg);

            resp.sendRedirect(req.getContextPath() + "/exer/list.do?petId=" + petId);

        } catch (Exception e) {
            req.getSession().setAttribute("flash", "등록 처리 중 오류: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/exer/list.do");
        }
    }

    private Integer resolveOwnerId(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return null;
        Object v = s.getAttribute("ownerId");
        if (v instanceof Integer i && i > 0) return i;
        OwnerVO o = (OwnerVO) s.getAttribute("loginUser");
        if (o != null) { s.setAttribute("ownerId", o.getOwnerId()); return o.getOwnerId(); }
        return null;
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
