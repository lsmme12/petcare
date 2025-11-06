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

@WebServlet("/join.do")
public class JoinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("owner/join.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name     = request.getParameter("name");
        String nickname = request.getParameter("nickname");
        String email    = request.getParameter("email");
        String userid   = request.getParameter("userid");
        String pwd      = request.getParameter("pwd");

        OwnerVO oVo = new OwnerVO();
        oVo.setName(name);
        oVo.setNickname(nickname);
        oVo.setEmail(email);
        oVo.setUserid(userid);
        oVo.setPwd(pwd);

        OwnerDAO dao = OwnerDAO.getInstance();
        int result = dao.insertOwner(oVo);

        HttpSession session = request.getSession();
        session.setAttribute("flashMessage",
            result == 1 ? "회원가입이 완료되었습니다." : "회원가입에 실패했습니다.");

        response.sendRedirect(request.getContextPath() + "/login.do");
    }

}

