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
import java.util.List;

@WebServlet("/obesity/list.do")
public class ObesityListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        int limit = parseInt(req.getParameter("limit"), 20);
        ObesityDAO dao = new ObesityDAO();
        List<ObesityDTO> rows = dao.listByPet(petId, limit);
        req.setAttribute("rows", rows);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/obesity/list.jsp");
        rd.forward(req, resp);
    }
    private int parseInt(String s,int d){ try{return Integer.parseInt(s);}catch(Exception e){return d;} }
}

