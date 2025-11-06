<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.petcare.dao.PetDAO,com.petcare.dto.PetDTO" %>
<%@ page import="java.util.List" %>
<%
  request.setCharacterEncoding("UTF-8");
  String ctx = request.getContextPath();
  Integer ownerId = (Integer) session.getAttribute("ownerId");
  if (ownerId == null) {
    response.sendRedirect(ctx + "/login.do");
    return;
  }

  List<PetDTO> pets = (List<PetDTO>) request.getAttribute("pets");
  if (pets == null) {
    PetDAO petDao = new PetDAO();
    pets = petDao.getPetsByOwner(ownerId);
  }
  if (pets == null) pets = java.util.Collections.emptyList();

  Integer petIdAttr = (Integer) request.getAttribute("petId");
  String petIdParam = request.getParameter("petId");
  int petId = (petIdAttr != null) ? petIdAttr : 0;
  try {
    if (petId == 0 && petIdParam != null) petId = Integer.parseInt(petIdParam);
  } catch (NumberFormatException ignore) { petId = 0; }

  PetDTO selectedPet = null;
  if (!pets.isEmpty()) {
    for (PetDTO p : pets) {
      if (p.getPetId() == petId) {
        selectedPet = p;
        break;
      }
    }
    if (selectedPet == null) {
      selectedPet = pets.get(0);
      petId = selectedPet.getPetId();
    }
  }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>투약기록 등록</title>
  <style>
    body { font-family: system-ui, sans-serif; padding: 24px; }
    input, button, label, select { font-size:16px; }
    .card { padding:16px; border:1px solid #e5e7eb; border-radius:12px; max-width:520px; }
    .row { margin-bottom: 12px; display:flex; gap:8px; align-items:center; }
    .muted { color:#6b7280; }
  </style>
</head>
<body>
  <h1>투약기록 등록</h1>
  <% if (pets.isEmpty()) { %>
    <p>등록된 반려동물이 없습니다. 반려동물을 먼저 등록해 주세요.</p>
    <p><a href="<%= ctx %>/main.do">메인으로</a></p>
  <% } else { %>
  <div class="card">
    <form action="<%= ctx %>/medi/create.do" method="post">
      <div class="row">
        <label for="petId">반려동물</label>
        <select id="petId" name="petId">
          <% for (PetDTO p : pets) { %>
            <option value="<%= p.getPetId() %>" <%= (p.getPetId() == petId) ? "selected" : "" %>>
              <%= p.getPetName() %>
            </option>
          <% } %>
        </select>
      </div>

      <div class="row">
        <label for="medicine">약품명</label>
        <input type="text" id="medicine" name="medicine" placeholder="예) 감기약 100mg" required maxlength="100">
      </div>

      <div class="row">
        <label for="dosageTime">투약시각</label>
        <input type="datetime-local" id="dosageTime" name="dosageTime" required>
      </div>

      <button type="submit">등록</button>
      <a href="<%= ctx %>/medi/list.do?petId=<%= petId %>">목록으로</a> | <a href="<%= ctx %>/main.do">메인</a>
    </form>
  </div>
  <% } %>
</body>
</html>
