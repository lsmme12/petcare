<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Pet 조회</title>
</head>
<body>
    <h2>운동기록 리스트</h2>
    <!-- pet_id 1 조회 버튼 -->
   <form action="${pageContext.request.contextPath}/exer_list.do" method="get">
    <input type="hidden" name="petId" value="1">
    <button type="submit">리스트 조회</button>
    </form>
   <form action="${pageContext.request.contextPath}/pet_view.do" method="get">
    <input type="hidden" name="petId" value="1">
    <button type="submit">리스트 조회</button>
</form>

 
<h4>해야할것:  각 강아지번호에 맞춰서 그 강아지 운동기록만 나오게하기, 프론트 강아지 케어 피그마로 디자인</h4>
</body>

</html>
