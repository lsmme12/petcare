package com.pet.controller;

import java.io.IOException;
import com.pet.dao.OwnerDAO;
import com.pet.dto.OwnerVO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ownerUpdate.do")
public class OwnerUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        OwnerVO vo = new OwnerVO();
        vo.setUserid(req.getParameter("userid"));   // ���� ���� (WHERE user_id=?)
        vo.setName(req.getParameter("name"));
        vo.setNickname(req.getParameter("nickname"));
        vo.setEmail(req.getParameter("email"));
        vo.setPwd(req.getParameter("pwd"));

        OwnerDAO oDao = OwnerDAO.getInstance();
        int updated = oDao.updateOwner(vo);

        if (updated == 1) {
            // ���ǿ� �г��� �� ����
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", vo);
            session.setAttribute("nickname", vo.getNickname());
            resp.sendRedirect("index.jsp?updated=1");
        } else {
            resp.sendRedirect("owner/ownerUpdate.jsp?error=1");
        }
    }
}
