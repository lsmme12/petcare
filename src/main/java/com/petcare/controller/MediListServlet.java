package com.petcare.controller;

import com.petcare.dao.MediRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.MediRecordDTO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/medi/list.do")
public class MediListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer ownerId = resolveOwnerId(req);
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int petId = parseInt(req.getParameter("petId"), 0);
        PetDAO petDao = new PetDAO();
        List<PetDTO> pets = petDao.getPetsByOwner(ownerId);
        if (pets == null) pets = Collections.emptyList();

        PetDTO selected = null;
        if (!pets.isEmpty()) {
            for (PetDTO p : pets) {
                if (p.getPetId() == petId) { selected = p; break; }
            }
            if (selected == null) { selected = pets.get(0); petId = selected.getPetId(); }
        } else {
            petId = 0;
        }

        List<MediRecordDTO> records = Collections.emptyList();
        if (petId > 0) {
            MediRecordDAO dao = new MediRecordDAO();
            records = dao.list(petId);
        }

        req.setAttribute("petId", petId);
        req.setAttribute("pet", selected);
        req.setAttribute("pets", pets);
        req.setAttribute("list", records);
        RequestDispatcher rd = req.getRequestDispatcher("/medi/medilist.jsp");
        rd.forward(req, resp);
    }

    private Integer resolveOwnerId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object v = session.getAttribute("ownerId");
        if (v instanceof Integer i && i > 0) return i;
        OwnerVO lv = (OwnerVO) session.getAttribute("loginUser");
        if (lv != null) { session.setAttribute("ownerId", lv.getOwnerId()); return lv.getOwnerId(); }
        return null;
    }

    private int parseInt(String s, int def) { try { return Integer.parseInt(s); } catch (Exception e) { return def; } }
}


