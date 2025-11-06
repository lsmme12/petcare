package com.petcare.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.petcare.dao.ExerRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.ExerRecordDTO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/exer/list.do")
public class ExerListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ExerRecordDAO exerDao = new ExerRecordDAO();
    private PetDAO petDao = new PetDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int petId = parseInt(req.getParameter("petId"), 0);
        List<PetDTO> pets = petDao.getPetsByOwner(ownerId);
        if (pets == null) pets = Collections.emptyList();

        PetDTO selected = null;
        boolean ownerMismatch = false;

        if (!pets.isEmpty()) {
            // URL petId가 owner 소유인지 확인
            for (PetDTO p : pets) {
                if (p.getPetId() == petId) {
                    selected = p;
                    break;
                }
            }
            if (selected == null) {
                // 소유자가 아닌 petId 접근
                ownerMismatch = true;
            }
        } else {
            ownerMismatch = true; // 소유한 pet 자체가 없음
        }

        // exerList 세팅
        List<ExerRecordDTO> exerList = Collections.emptyList();
        if (selected != null) {
            exerList = exerDao.getRecordsByPetId(selected.getPetId());
        }

        // JSP에 전달
        req.setAttribute("petId", petId);
        req.setAttribute("pet", selected);
        req.setAttribute("pets", pets);
        req.setAttribute("exerList", exerList);
        req.setAttribute("ownerMismatch", ownerMismatch);

        RequestDispatcher rd = req.getRequestDispatcher("/exer/exer_list.jsp");
        rd.forward(req, resp);
    }

    private Integer resolveOwnerId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object v = session.getAttribute("ownerId");
        if (v instanceof Integer i && i > 0) return i;
        OwnerVO lv = (OwnerVO) session.getAttribute("loginUser");
        if (lv != null) {
            session.setAttribute("ownerId", lv.getOwnerId());
            return lv.getOwnerId();
        }
        return null;
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
