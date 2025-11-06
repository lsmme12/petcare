package com.petcare.controller;

import com.petcare.dao.ObesityDAO;
import com.petcare.dto.ObesityDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/obesity/latest.do")
public class ObesityLatestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        ObesityDAO dao = new ObesityDAO();
        ObesityDTO row = dao.latestByPet(petId);
        req.setAttribute("row", row);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/obesity/latest.jsp");
        rd.forward(req, resp);
    }
    private int parseInt(String s,int d){ try{return Integer.parseInt(s);}catch(Exception e){return d;} }
}

