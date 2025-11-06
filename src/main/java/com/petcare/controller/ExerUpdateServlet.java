package com.petcare.controller;

import java.io.IOException;

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

@WebServlet("/exer/update.do")
public class ExerUpdateServlet extends HttpServlet {
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
        	int exerId = parseInt(req.getParameter("exer_id"), 0);
        	int petId = parseInt(req.getParameter("petId"), 0);

        	// 날짜 파라미터 처리
        	String exerDateStr = req.getParameter("exer_date");
        	java.sql.Date exerDate = null;
        	if (exerDateStr != null && !exerDateStr.isEmpty()) {
        	    exerDate = java.sql.Date.valueOf(exerDateStr);
        	} else {
        	    throw new ServletException("운동 날짜는 필수 입력입니다.");
        	}

        	int exerTime = parseInt(req.getParameter("exer_time"), 0);
        	String exerLevel = req.getParameter("exer_level");
        	String memo = req.getParameter("memo");


            PetDTO pet = petDao.getPetById(petId);
            if (pet == null || pet.getOwnerId() != ownerId) {
                req.getSession().setAttribute("flash", "권한이 없는 반려동물 접근입니다.");
                resp.sendRedirect(req.getContextPath() + "/exer/list.do");
                return;
            }

            ExerRecordDTO dto = new ExerRecordDTO();
            dto.setExerId(exerId);
            dto.setPetId(petId);
            dto.setExerDate(exerDate);  // java.sql.Date 타입
            dto.setExerTime(exerTime);
            dto.setExerLevel(exerLevel);
            dto.setMemo(memo);

            dao.updateRecord(dto);

            req.getSession().setAttribute("flash", "운동 기록이 수정되었습니다.");
            resp.sendRedirect(req.getContextPath() + "/exer/list.do?petId=" + petId);

        } catch (Exception e) {
            req.getSession().setAttribute("flash", "수정 처리 중 오류: " + e.getMessage());
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

    private int parseInt(String s, int d) {
        try { return Integer.parseInt(s); } catch (Exception e) { return d; }
    }
}
