package com.petcare.controller;

import java.io.IOException;
import java.util.List;

import com.petcare.dao.ExerRecordDAO;
import com.petcare.dao.PetDAO;
import com.petcare.dto.ExerRecordDTO;
import com.petcare.dto.OwnerVO;
import com.petcare.dto.PetDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/care/main.do")
public class CareMainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PetDAO petDao = new PetDAO();           // 🐾 Pet 조회용 DAO
    private ExerRecordDAO exerDao = new ExerRecordDAO(); // 🏃 운동 기록 조회용 DAO

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1️⃣ 로그인 체크
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.do");
            return;
        }

        OwnerVO loginUser = (OwnerVO) session.getAttribute("loginUser");
        int ownerId = loginUser.getOwnerId();

        // 2️⃣ petId 파라미터 처리 (기본값 0)
        
        String petIdParam = req.getParameter("petId");
        int petId = 0;
        try {
            petId = Integer.parseInt(petIdParam);
        } catch (NumberFormatException e) {
            petId = 0; // 파라미터 없거나 잘못된 값이면 첫 번째 펫으로
        }

        // 3️⃣ owner 소유 펫 리스트 조회
        List<PetDTO> ownerPets = petDao.getPetsByOwner(ownerId);

        // 4️⃣ 선택된 펫 결정 (URL petId가 owner 소유인지 확인)
        PetDTO selectedPet = null;
        if (!ownerPets.isEmpty()) {
            for (PetDTO p : ownerPets) {
                if (p.getPetId() == petId) {
                    selectedPet = p;
                    break;
                }
            }
            if (selectedPet == null) { // URL petId가 없거나 owner 소유가 아닌 경우
                selectedPet = ownerPets.get(0);
                petId = selectedPet.getPetId();
            }
        }

        // 5️⃣ 운동 기록 조회 (선택된 pet 기준)
        List<ExerRecordDTO> exerList = selectedPet != null ? exerDao.getRecordsByPetId(petId) : null;

     // 6️⃣ owner 소유 펫 기준 이전 / 다음 펫 찾기
        boolean nextExists = false;
        boolean prevExists = false;
        int nextPetId = 0;
        int prevPetId = 0;

        if (selectedPet != null) {
            int currentId = selectedPet.getPetId();

            for (PetDTO p : ownerPets) {
                int id = p.getPetId();

                if (id > currentId) { // 현재보다 큰 = 다음 후보
                    if (nextPetId == 0 || id < nextPetId) {
                        nextPetId = id;
                    }
                }
                if (id < currentId) { // 현재보다 작은 = 이전 후보
                    if (prevPetId == 0 || id > prevPetId) {
                        prevPetId = id;
                    }
                }
            }

            nextExists = (nextPetId != 0);
            prevExists = (prevPetId != 0);
        }


        // 7️⃣ request 속성 세팅
        req.setAttribute("pet", selectedPet);
        req.setAttribute("exerList", exerList);
        req.setAttribute("nextExists", nextExists);
        req.setAttribute("nextPetId", nextPetId);
        req.setAttribute("prevExists", prevExists);
        req.setAttribute("prevPetId", prevPetId);

        // 8️⃣ JSP 포워딩
        req.getRequestDispatcher("/care/caremain.jsp").forward(req, resp);
    }
}
