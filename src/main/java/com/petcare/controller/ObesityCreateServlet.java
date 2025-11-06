package com.petcare.controller;

import com.petcare.dao.ObesityDAO;
import com.petcare.dto.ObesityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/obesity/create.do")
public class ObesityCreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int petId = parseInt(req.getParameter("petId"), 0);
        String c1 = nz(req.getParameter("check_1"), "N");
        String c2 = nz(req.getParameter("check_2"), "N");
        String c3 = nz(req.getParameter("check_3"), "N");
        String c4 = nz(req.getParameter("check_4"), "N");
        String c5 = nz(req.getParameter("check_5"), "N");
        String msg;
        try {
            ObesityDTO dto = new ObesityDTO();
            dto.setPetId(petId);
            dto.setCheck1(c1);
            dto.setCheck2(c2);
            dto.setCheck3(c3);
            dto.setCheck4(c4);
            dto.setCheck5(c5);
            ObesityDAO dao = new ObesityDAO();
            int r = dao.insert(dto);
            msg = (r>0) ? "비만도 기록이 저장되었습니다." : "저장 실패";
        } catch (Exception e) {
            msg = "오류: "+e.getMessage();
        }
        req.getSession().setAttribute("flash", msg);
        resp.sendRedirect(req.getContextPath()+"/obesity/list.do?petId="+petId);
    }
    private int parseInt(String s,int d){ try{return Integer.parseInt(s);}catch(Exception e){return d;} }
    private String nz(String s,String d){ return (s==null||s.trim().isEmpty())? d: s.trim(); }
}

