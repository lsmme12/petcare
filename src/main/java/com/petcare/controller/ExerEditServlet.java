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

@WebServlet("/exer/edit.do")
public class ExerEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ExerRecordDAO dao = new ExerRecordDAO();
    private PetDAO petDao = new PetDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int exerId = parseInt(req.getParameter("exerId"), 0);
        if (exerId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/exer/list.do");
            return;
        }

        try {
            ExerRecordDTO record = dao.getRecordById(exerId);
            if (record == null) throw new IllegalArgumentException("해당 운동 기록이 존재하지 않습니다.");

            PetDTO pet = petDao.getPetById(record.getPetId());
            if (pet == null || pet.getOwnerId() != ownerId) {
                throw new IllegalArgumentException("권한이 없는 반려동물 접근입니다.");
            }

            req.setAttribute("record", record);
            req.getRequestDispatcher("/exer/exer_edit.jsp").forward(req, resp);

        } catch (Exception e) {
            req.getSession().setAttribute("flash", "운동 기록 조회 중 오류: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/exer/list.do?petId=" + parseInt(req.getParameter("petId"), 0));
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
