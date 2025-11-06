package com.petcare.controller;

import java.io.IOException;

import com.petcare.dao.PetDAO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/exer/form.do")
public class ExerFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PetDAO petDao = new PetDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 로그인 사용자 확보
        HttpSession session = req.getSession(false);
        OwnerVO loginUser = (session != null) ? (OwnerVO) session.getAttribute("loginUser") : null;
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }
        int ownerId = loginUser.getOwnerId();

        // 파라미터에서 petId 받기
        int petId = parseInt(req.getParameter("petId"), 0);
        if (petId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/care/main.do");
            return;
        }

        // petId 가 로그인한 owner의 pet인지 검사
        PetDTO pet = petDao.getPetById(petId);
        if (pet == null || pet.getOwnerId() != ownerId) {
            req.getSession().setAttribute("flash", "⚠️ 권한이 없는 반려동물입니다.");
            resp.sendRedirect(req.getContextPath() + "/care/main.do");
            return;
        }

        // JSP로 넘기기
        req.setAttribute("pet", pet);
        req.getRequestDispatcher("/exer/exer_form.jsp").forward(req, resp);
    }

    private int parseInt(String s, int fallback) {
        try { return Integer.parseInt(s); } catch (Exception e) { return fallback; }
    }
}
