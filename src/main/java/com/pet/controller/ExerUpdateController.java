package com.pet.controller;

import java.io.IOException;
import java.sql.Date;

import com.pet.dao.ExerRecordDAO;
import com.pet.dto.ExerRecordDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExerUpdateController {
    private ExerRecordDAO dao = new ExerRecordDAO();

    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int exerId = Integer.parseInt(request.getParameter("exer_id"));
            Date exerDate = Date.valueOf(request.getParameter("exer_date"));
            int exerTime = Integer.parseInt(request.getParameter("exer_time"));
            String exerLevel = request.getParameter("exer_level");
            String memo = request.getParameter("memo");

            ExerRecordDTO dto = new ExerRecordDTO();
            dto.setExerId(exerId);
            dto.setExerDate(exerDate);
            dto.setExerTime(exerTime);
            dto.setExerLevel(exerLevel);
            dto.setMemo(memo);

            dao.updateRecord(dto);
            response.sendRedirect(request.getContextPath() + "/exer_list.do");

        } catch (Exception e) {
            throw new ServletException("수정 처리 중 오류: " + e.getMessage(), e);
        }
    }
}
