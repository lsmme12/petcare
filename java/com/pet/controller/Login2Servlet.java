package com.pet.controller;

import java.io.IOException;
import com.pet.dao.OwnerDAO;
import com.pet.dto.OwnerVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login2.do")
public class Login2Servlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    String userid = request.getParameter("userid");
    String pwd    = request.getParameter("pwd");

    OwnerDAO dao = OwnerDAO.getInstance();
    OwnerVO  oVo = dao.getOwnerById(userid);

    if (oVo != null && oVo.getPwd().equals(pwd)) {
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", oVo);
      session.setAttribute("nickname", oVo.getNickname());
      try {
        Integer ownerId = OwnerDAO.getInstance().getOwnerIdByUserId(userid);
        if (ownerId != null) session.setAttribute("ownerId", ownerId);
      } catch (Exception ignore) {}
      response.sendRedirect("index.jsp");
    } else {
      response.sendRedirect("owner/login.jsp?error=1");
    }
  }
}

