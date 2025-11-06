package com.petcare.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ExerciseServlet", urlPatterns = {
        "/exer_list.do",
        "/exer_form.do",
        "/exer_insert.do",
        "/exer_edit.do",
        "/exer_update.do",
        "/exer_delete.do"
})
public class ExerciseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ExerListController listController = new ExerListController();
    private final ExerInsertController insertController = new ExerInsertController();
    private final ExerEditController editController = new ExerEditController();
    private final ExerUpdateController updateController = new ExerUpdateController();
    private final ExerDeleteController deleteController = new ExerDeleteController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/exer_list.do":
                listController.handle(req, resp);
                break;
            case "/exer_form.do":
                req.getRequestDispatcher("/views/exer_form.jsp").forward(req, resp);
                break;
            case "/exer_edit.do":
                editController.handle(req, resp);
                break;
            case "/exer_delete.do":
                deleteController.handle(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/exer_insert.do":
                insertController.handle(req, resp);
                break;
            case "/exer_update.do":
                updateController.handle(req, resp);
                break;
            case "/exer_delete.do":
                deleteController.handle(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}