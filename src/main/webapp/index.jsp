<%@page import="fm.last.citrine.service.SettingManager"%>
<%@page import="fm.last.citrine.service.UserManager"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
UserManager userManager = (UserManager)context.getBean("userManager");

userManager.setCurrentUser(null);
getServletContext().removeAttribute("username");

SettingManager settingManager = (SettingManager)context.getBean("settingManager");

if("maintenance".equals(settingManager.get("status").getVal()))
{
	response.sendRedirect("maintenance.jsp");
	return;
}
%>
<html>
  <head>
    <title>Job Scheduler @ modal server</title>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
  </head>
  <body>
  	<%@include file="header-full.jsp" %>
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