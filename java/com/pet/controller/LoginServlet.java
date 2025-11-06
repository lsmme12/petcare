package com.pet.controller;

import java.io.IOException;
import com.pet.dao.OwnerDAO;
import com.pet.dto.OwnerVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
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
      session.setAttribute("loginUser", oVo);              // 전체 객체
      session.setAttribute("nickname", oVo.getNickname()); // 선택
      response.sendRedirect("main.jsp");                   // ✅ 로그인 후 페이지
    } else {
      response.sendRedirect("owner/login.jsp?error=1");
    }
  }
}
