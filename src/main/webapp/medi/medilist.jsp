<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.petcare.dto.MediRecordDTO" %>
<%@ page import="com.petcare.dto.PetDTO" %>
<%@ page import="java.util.List" %>
<%
    Integer petIdObj = (Integer) request.getAttribute("petId");
    int petId = petIdObj != null ? petIdObj : 0;
    PetDTO selectedPet = (PetDTO) request.getAttribute("pet");
    List<PetDTO> pets = (List<PetDTO>) request.getAttribute("pets");
    List<MediRecordDTO> records = (List<MediRecordDTO>) request.getAttribute("list");
    if (pets == null) pets = java.util.Collections.emptyList();
    if (records == null) records = java.util.Collections.emptyList();
    String flash = (String) session.getAttribute("flash");
    if (flash != null) { session.removeAttribute("flash"); }
    String petName = (selectedPet != null && selectedPet.getPetName() != null && !selectedPet.getPetName().isEmpty())
        ? selectedPet.getPetName() : "선택한 반려동물 없음";
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>투약기록 목록</title>
  <style>
    body { font-family: system-ui, sans-serif; padding: 24px; }
    .medi-list { list-style: none; padding: 0; margin: 0; max-width: 720px; }
    .medi-list li { border: 1px solid #e5e7eb; border-radius: 8px; padding: 10px 12px; margin-bottom: 8px; }
    .medi-item { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; align-items: center; }
    .medi-item .label { color:#6b7280; margin-right:6px; }
    .actions { margin-top: 6px; }
    .actions form { display:inline; }
    .actions button { margin-right:8px; }
    .flash { margin-bottom:16px; padding:10px 12px; border-radius:8px; background:#fff8e6; color:#5b4030; max-width:720px; }
    .toolbar { margin: 12px 0; display:flex; gap:12px; align-items:center; }
    select { padding:6px 10px; }
  </style>
  </head>
<body>
  <h1>투약기록 목록</h1>
  <% if (flash != null) { %>
    <div class="flash"><%= flash %></div>
  <% } %>

  <% if (pets.isEmpty()) { %>
    <p>등록된 반려동물이 없습니다. 반려동물을 등록하신 뒤 투약기록을 작성해 주세요.</p>
    <p><a href="<%= ctx %>/main.do">메인으로 돌아가기</a></p>
  <% } else { %>
    <div class="toolbar">
      <form method="get" action="<%= ctx %>/medi/list.do">
        <label for="petSelect">반려동물 선택</label>
        <select id="petSelect" name="petId" onchange="this.form.submit()">
          <% for (PetDTO p : pets) { %>
            <option value="<%= p.getPetId() %>" <%= (selectedPet != null && p.getPetId() == selectedPet.getPetId()) ? "selected" : "" %>>
              <%= p.getPetName() %>
            </option>
          <% } %>
        </select>
      </form>
      <a href="<%= ctx %>/medi/add.do?petId=<%= petId %>">+ 투약기록 등록</a>
      <a href="<%= ctx %>/main.do">메인</a>
    </div>

    <p>반려동물: <strong><%= petName %></strong></p>

    <ul class="medi-list">
      <% if (records.isEmpty()) { %>
        <li>등록된 투약기록이 없습니다.</li>
      <% } else {
           for (MediRecordDTO record : records) { %>
        <li>
          <div class="medi-item">
            <div><span class="label">Record ID:</span><%= record.getRecordId() %></div>
            <div><span class="label">약품명:</span><%= record.getMedicine() %></div>
            <div><span class="label">투약시각:</span><%= record.getDosageTimeFormatted() %></div>
            <div class="actions">
              <form action="<%= ctx %>/medi/delete.do" method="post" onsubmit="return confirm('삭제하시겠습니까?');">
                <input type="hidden" name="recordId" value="<%= record.getRecordId() %>">
                <input type="hidden" name="petId" value="<%= petId %>">
                <button type="submit">삭제</button>
              </form>
            </div>
          </div>
        </li>
      <%   }
         } %>
    </ul>
  <% } %>
</body>
</html>
