package com.pet.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Collections;

import com.pet.dao.ExerRecordDAO;
import com.pet.dto.ExerRecordDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExerInsertController {

    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            String petIdStr = request.getParameter("pet_id");
            String exerDateStr = request.getParameter("exer_date");
            String exerTimeStr = request.getParameter("exer_time");
            String exerLevel = request.getParameter("exer_level");
            String memo = request.getParameter("memo");

            if (petIdStr == null || exerDateStr == null || exerTimeStr == null ||
                petIdStr.isEmpty() || exerDateStr.isEmpty() || exerTimeStr.isEmpty()) {
                throw new ServletException("필수 입력값이 누락되었습니다.");
            }

            int petId = Integer.parseInt(petIdStr);
            Date exerDate = Date.valueOf(exerDateStr);
            int exerTime = Integer.parseInt(exerTimeStr);

            ExerRecordDTO record = new ExerRecordDTO();
            record.setPetId(petId);
            record.setExerDate(exerDate);
            record.setExerTime(exerTime);
            record.setExerLevel(exerLevel != null ? exerLevel : "");
            record.setMemo(memo != null ? memo : "");

            ExerRecordDAO dao = new ExerRecordDAO();
            int[] result = dao.insertRecordsBatch(Collections.singletonList(record));

            if (result.length > 0 && result[0] > 0) {
                response.sendRedirect(request.getContextPath() + "/exer_list.do");
            } else {
                response.getWriter().write("<h3>등록 실패: DB insert 오류</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("등록 처리 중 오류: " + e.getMessage(), e);
        }
    }
}
