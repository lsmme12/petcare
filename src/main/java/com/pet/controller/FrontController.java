package com.pet.controller;

import java.io.IOException;

import com.pet.dao.PetDAO;
import com.pet.dto.PetDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("*.do")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());

        switch (action) {

            case "/exer_list.do":
                new ExerListController().handle(request, response);
                break;

            case "/exer_form.do":
                request.getRequestDispatcher("/WEB-INF/views/exer_form.jsp").forward(request, response);
                break;

            case "/exer_insert.do":
                new ExerInsertController().handle(request, response);
                break;

            case "/exer_edit.do":
                new ExerEditController().handle(request, response);
                break;

            case "/exer_update.do":
                new ExerUpdateController().handle(request, response);
                break;

            case "/exer_delete.do":
                new ExerDeleteController().handle(request, response);
                break;

            case "/pet_view.do":
            	String petIdParam = request.getParameter("petId");
            	int petId = (petIdParam != null) ? Integer.parseInt(petIdParam) : 1;
            	System.out.println("Servlet - 요청받은 petId = " + petId);


                PetDAO petDao = new PetDAO();
                PetDTO pet = petDao.getPetById(petId);
                boolean nextExists = petDao.nextPetExists(petId);

                if (pet != null) {
                    System.out.println("Servlet - petId=" + pet.getPetId() 
                        + ", petName=" + pet.getPetName() 
                        + ", sex=" + pet.getSex());
                } else {
                    System.out.println("Servlet - pet object is null!");
                }

                request.setAttribute("pet", pet);
                request.setAttribute("nextExists", nextExists);
                request.getRequestDispatcher("/WEB-INF/views/pet_view.jsp").forward(request, response);
                
                break;

            default:
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                break;
        }
    }
}
