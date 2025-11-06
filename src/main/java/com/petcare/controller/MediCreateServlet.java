package com.petcare.controller;

import com.petcare.dao.MediRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.MediRecordDTO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@WebServlet("/medi/create.do")
public class MediCreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int petId = parseInt(req.getParameter("petId"), 0);
        PetDAO petDao = new PetDAO();
        PetDTO pet = petDao.getPetById(petId);
        String msg;
        if (pet == null || pet.getOwnerId() != ownerId) {
            msg = "잘못된 반려동물입니다.";
            req.getSession().setAttribute("flash", msg);
            resp.sendRedirect(req.getContextPath() + "/medi/list.do");
            return;
        }

        String medicine = nz(req.getParameter("medicine"));
        String dosageTime = nz(req.getParameter("dosageTime"));
        try {
            if (medicine.isEmpty()) throw new IllegalArgumentException("약품명을 입력해 주세요.");
            if (dosageTime.isEmpty()) throw new IllegalArgumentException("투약시각을 입력해 주세요.");
            LocalDateTime ldt = LocalDateTime.parse(dosageTime);
            Timestamp ts = Timestamp.valueOf(ldt);
            MediRecordDTO dto = new MediRecordDTO();
            dto.setMedicine(medicine);
            dto.setDosageTime(new Date(ts.getTime()));
            MediRecordDAO dao = new MediRecordDAO();
            int r = dao.insert(dto, petId);
            msg = (r > 0) ? "등록되었습니다." : "등록 실패(변경 없음).";
        } catch (Exception e) {
            msg = "처리 오류: " + e.getMessage();
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

    private String nz(String s) {
        return s == null ? "" : s.trim();
    }
}
