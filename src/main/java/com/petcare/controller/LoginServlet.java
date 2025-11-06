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

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/owner/login.jsp").forward(request, response);
  }

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
      session.setAttribute("ownerId", oVo.getOwnerId());
      response.sendRedirect(request.getContextPath() + "/main.do");
    } else {
      response.sendRedirect(request.getContextPath() + "/login.do?error=1");
    }
  }
}
