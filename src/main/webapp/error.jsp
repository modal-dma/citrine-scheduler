<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JOB SCHEDULER @ MODAL </title>
<link rel="stylesheet" href="css/generic.css" type="text/css"/>
</head>
<body>
<%@include file="header.jsp" %>
<br/>
<br/>
<center>
<h1>Errore</h1>
<%
String error = (String)getServletContext().getAttribute("error");

%>
<h2><%=error%></h2>

<a href="" onclick="window.history.back();">Back</a>
</center>
</body>
</html>