package com.petcare.controller;

import java.io.IOException;

import com.petcare.dao.ExerRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/exer/delete.do")
public class ExerDeleteServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExerRecordDAO dao = new ExerRecordDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int petId = parseInt(req.getParameter("petId"), 0);
        int exerId = parseInt(req.getParameter("exerId"), 0);
        String msg;

        try {
            PetDAO petDao = new PetDAO();
            PetDTO pet = petDao.getPetById(petId);
            if (pet == null || pet.getOwnerId() != ownerId) {
                throw new IllegalArgumentException("권한이 없는 반려동물 접근입니다.");
            }

            int r = dao.deleteRecord(exerId);
            msg = (r > 0) ? "운동 기록이 삭제되었습니다." : "삭제 실패 (존재하지 않거나 이미 삭제됨).";
        } catch (Exception e) {
            msg = "삭제 중 오류 발생: " + e.getMessage();
        }

        req.getSession().setAttribute("flash", msg);
        resp.sendRedirect(req.getContextPath() + "/exer/list.do?petId=" + petId);
    }

    private Integer resolveOwnerId(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return null;

        Object v = s.getAttribute("ownerId");
        if (v instanceof Integer i && i > 0) return i;

        OwnerVO o = (OwnerVO) s.getAttribute("loginUser");
        if (o != null) {
            s.setAttribute("ownerId", o.getOwnerId());
            return o.getOwnerId();
        }

        return null;
    }

    private int parseInt(String s, int d) {
        try { return Integer.parseInt(s); } catch (Exception e) { return d; }
    }
}
