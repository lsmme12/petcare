<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.*" %>
<%
try {
    Context initCtx = new InitialContext();
    Context envCtx = (Context)initCtx.lookup("java:/comp/env");
    DataSource ds = (DataSource)envCtx.lookup("jdbc/myoracle");
    Connection conn = ds.getConnection();
    out.println("✅ DBCP 연결 성공!");
    conn.close();
} catch (Exception e) {
    out.println("❌ DBCP 연결 실패: " + e.getMessage());
    e.printStackTrace();
}
%>


</body>
</html>