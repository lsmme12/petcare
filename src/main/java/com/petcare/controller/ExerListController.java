package com.petcare.controller;

import java.io.IOException;
import java.util.List;

import com.petcare.dao.ExerRecordDAO;
import com.petcare.dto.ExerRecordDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExerListController {
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ExerRecordDAO dao = new ExerRecordDAO();
        List<ExerRecordDTO> list = dao.getAllRecords();

        request.setAttribute("list", list);
        request.getRequestDispatcher("/views/exer_list.jsp").forward(request, response);
    }
}