package com.petcare.controller;

import java.io.IOException;

import com.petcare.dao.ExerRecordDAO;
import com.petcare.dto.ExerRecordDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExerEditController {
    private final ExerRecordDAO dao = new ExerRecordDAO();

    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("exer_id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/exer_list.do");
            return;
        }

        int exerId = Integer.parseInt(idStr);
        ExerRecordDTO record = dao.getRecordById(exerId);

        request.setAttribute("record", record);
        request.getRequestDispatcher("/views/exer_edit.jsp").forward(request, response);
    }
}