package com.petcare.controller;

import java.io.IOException;
import com.petcare.dao.OwnerDAO;
import com.petcare.dto.OwnerVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ownerUpdate.do")
public class OwnerUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        OwnerVO vo = new OwnerVO();
        vo.setUserid(req.getParameter("userid"));
        vo.setName(req.getParameter("name"));
        vo.setNickname(req.getParameter("nickname"));
        vo.setEmail(req.getParameter("email"));
        vo.setPwd(req.getParameter("pwd"));

        OwnerDAO oDao = OwnerDAO.getInstance();
        int updated = oDao.updateOwner(vo);

        if (updated == 1) {
            HttpSession session = req.getSession();
            Integer ownerId = (Integer) session.getAttribute("ownerId");
            if (ownerId != null) {
                vo.setOwnerId(ownerId);
            }
            session.setAttribute("loginUser", vo);
            session.setAttribute("nickname", vo.getNickname());
            if (ownerId != null) {
                session.setAttribute("ownerId", ownerId);
            }
            resp.sendRedirect(req.getContextPath() + "/main.do");
        } else {
            resp.sendRedirect(req.getContextPath() + "/owner/update.do?error=1");
        }
    }
}

