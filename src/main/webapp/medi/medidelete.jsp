<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.petcare.dao.MediRecordDAO" %>
<%@ page import="com.petcare.dao.PetDAO" %>
<%@ page import="com.petcare.dto.PetDTO" %>
<%
    request.setCharacterEncoding("UTF-8");
    String ctx = request.getContextPath();
    Integer ownerId = (Integer) session.getAttribute("ownerId");
    if (ownerId == null) {
        response.sendRedirect(ctx + "/login.do");
        return;
    }

    String recordIdStr = request.getParameter("recordId");
    String petIdStr = request.getParameter("petId");
    String redirect = ctx + "/medi/list.do";
    String msg;
    try {
        int recordId = Integer.parseInt(recordIdStr);
        int petId = Integer.parseInt(petIdStr);

        PetDAO petDao = new PetDAO();
        PetDTO pet = petDao.getPetById(petId);
        if (pet == null || pet.getOwnerId() != ownerId) {
            throw new IllegalArgumentException("잘못된 반려동물입니다.");
        }

        MediRecordDAO dao = new MediRecordDAO();
        int r = dao.delete(recordId);
        msg = (r > 0) ? "삭제되었습니다." : "삭제 실패(변경 없음).";
        redirect = ctx + "/medi/list.do?petId=" + petId;
    } catch (Exception e) {
        msg = "삭제 오류: " + e.getMessage();
        if (petIdStr != null) {
            redirect = ctx + "/medi/list.do?petId=" + petIdStr;
        }
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>삭제 결과</title>
  <meta http-equiv="refresh" content="1; url=<%=redirect%>">
</head>
<body>
  <p><%= msg %></p>
  <p><a href="<%= redirect %>">이동</a></p>
</body>
</html>
