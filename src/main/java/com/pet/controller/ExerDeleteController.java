package com.pet.controller;

import java.io.IOException;

import com.pet.dao.ExerRecordDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExerDeleteController {
    private ExerRecordDAO dao = new ExerRecordDAO();

    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int exerId = Integer.parseInt(request.getParameter("exer_id"));
        dao.deleteRecord(exerId);
        response.sendRedirect(request.getContextPath() + "/exer_list.do");
    }
}
