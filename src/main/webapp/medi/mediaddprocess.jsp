<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.petcare.dao.MediRecordDAO" %>
<%@ page import="com.petcare.dao.PetDAO" %>
<%@ page import="com.petcare.dto.MediRecordDTO" %>
<%@ page import="com.petcare.dto.PetDTO" %>
<%@ page import="java.time.LocalDateTime,java.time.format.DateTimeParseException,java.sql.Timestamp,java.util.Date" %>
<%
    request.setCharacterEncoding("UTF-8");
    String ctx = request.getContextPath();
    Integer ownerId = (Integer) session.getAttribute("ownerId");
    if (ownerId == null) {
        response.sendRedirect(ctx + "/login.do");
        return;
    }

    String petIdStr = request.getParameter("petId");
    String medicine = request.getParameter("medicine");
    String dosageTimeStr = request.getParameter("dosageTime");

    String msg;
    String redirect;

    try {
        int petId = Integer.parseInt(petIdStr);
        PetDAO petDao = new PetDAO();
        PetDTO pet = petDao.getPetById(petId);
        if (pet == null || pet.getOwnerId() != ownerId) {
            throw new IllegalArgumentException("잘못된 반려동물입니다.");
        }

        if (medicine == null || medicine.trim().isEmpty()) {
            throw new IllegalArgumentException("약품명을 입력해 주세요.");
        }

        LocalDateTime ldt = LocalDateTime.parse(dosageTimeStr);
        Date utilDate = new Date(Timestamp.valueOf(ldt).getTime());

        MediRecordDTO dto = new MediRecordDTO();
        dto.setMedicine(medicine.trim());
        dto.setDosageTime(utilDate);

        MediRecordDAO dao = new MediRecordDAO();
        int r = dao.insert(dto, petId);
        if (r > 0) {
            msg = "등록되었습니다.";
            redirect = ctx + "/medi/list.do?petId=" + petId;
        } else {
            msg = "등록 실패(변경 없음).";
            redirect = ctx + "/medi/add.do?petId=" + petId;
        }
    } catch (NumberFormatException e) {
        msg = "Pet ID 형식이 올바르지 않습니다.";
        redirect = ctx + "/medi/list.do";
    } catch (DateTimeParseException e) {
        msg = "투약시각 형식이 올바르지 않습니다.";
        redirect = ctx + "/medi/add.do?petId=" + (petIdStr != null ? petIdStr : "");
    } catch (Exception e) {
        msg = "처리 오류: " + e.getMessage();
        redirect = ctx + "/medi/add.do?petId=" + (petIdStr != null ? petIdStr : "");
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>등록 결과</title>
  <meta http-equiv="refresh" content="1; url=<%=redirect%>">
</head>
<body>
  <p><%= msg %></p>
  <p><a href="<%= redirect %>">이동</a></p>
</body>
</html>
