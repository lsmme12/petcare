package com.petcare.controller;

import com.petcare.dao.MediRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/medi/delete.do")
public class MediDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }
        int petId = parseInt(req.getParameter("petId"), 0);
        int recordId = parseInt(req.getParameter("recordId"), 0);
        String msg;
        try {
            PetDAO petDao = new PetDAO();
            PetDTO pet = petDao.getPetById(petId);
            if (pet == null || pet.getOwnerId() != ownerId) {
                throw new IllegalArgumentException("잘못된 반려동물입니다.");
            }
            MediRecordDAO dao = new MediRecordDAO();
            int r = dao.delete(recordId);
            msg = (r > 0) ? "삭제되었습니다." : "삭제 실패(변경 없음).";
        } catch (Exception e) {
            msg = "삭제 오류: " + e.getMessage();
        }
        req.getSession().setAttribute("flash", msg);
        resp.sendRedirect(req.getContextPath() + "/medi/list.do?petId=" + petId);
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
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return d;
        }
    }
}
