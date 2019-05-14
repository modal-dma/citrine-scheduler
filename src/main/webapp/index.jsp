<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
  <head>
    <title>Job Scheduler @ modal server</title>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
  </head>
  <body>
  	<%@include file="header.jsp" %>
    <center>
    <h3>Login</h3>
    <form method="POST" action="login.jsp">
    
    <table style="width: auto;">
    <tr>
    <td>Username:</td>
    <td>
    <input name="username" type="text"/>
    </td>
    </tr>
    <tr>
    <td>Password:</td>
    <td>
    <input name="password" type="password"/>
    </td>
    </tr>    
    </table>
    
    <br/>
    <input type="submit" value="Login"/>
    </form>
    </center>
  </body>
</html>