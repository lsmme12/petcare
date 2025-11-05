<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
<script type="text/javascript" src="../script/owner.js"></script>
</head>
<body>
<h2>회원 정보 수정</h2>
<form action="${pageContext.request.contextPath}/ownerUpdate.do" method="post" name="frm">
    <table>
        <tr>
            <td>이름</td>
            <td><input type="text" name="name" size="20" value="${oVo.name}"></td>
        </tr>
        <tr>
            <td>닉네임</td>
            <td><input type="text" name="nickname" size="20" value="${oVo.nickname}"></td>
        </tr>
        <tr>
            <td>아이디</td>
            <td><input type="text" name="userid" size="20" value="${oVo.userid}" readonly></td>
        </tr>
        <tr>
            <td>비밀번호</td>
            <td><input type="password" name="pwd" size="20" placeholder="새 비밀번호 입력">*</td>
        </tr>
        <tr>
            <td>비밀번호 확인</td>
            <td><input type="password" name="pwd_check" size="20" placeholder="비밀번호 확인 입력">*</td>
        </tr>
        <tr>
            <td>이메일</td>
            <td><input type="email" name="email" size="20" value="${oVo.email}"></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" value="확인" onclick="return ownerJoinCheck()">
                <input type="reset" value="취소">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
