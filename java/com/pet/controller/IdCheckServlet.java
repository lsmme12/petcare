package com.pet.controller;

import java.io.IOException;
import com.pet.dao.OwnerDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/idCheck.do")
public class IdCheckServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String userid = request.getParameter("userid");
    String result = "INVALID";

    if(userid != null && !userid.trim().isEmpty()) {
      boolean exists = OwnerDAO.getInstance().confirmID(userid.trim());
      result = exists ? "FAIL" : "OK";
    }

    request.setAttribute("userid", userid);
    request.setAttribute("result", result);
    RequestDispatcher rd = request.getRequestDispatcher("/owner/idcheck.jsp");
    rd.forward(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
