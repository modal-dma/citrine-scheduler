<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="fm.last.citrine.model.User"%>
<%@page import="fm.last.citrine.service.UserManager"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
  <head>
    <title>Citrine</title>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
  </head>
  <body>
    <h2>Citrine</h2>
    <ul>
      <li> <a href="tasks.do">Tasks</a> - configure and monitor. </li>
      <li> Task Run Logs
        <ul>
          <li><a href="logs.do">HTML view</a>.</li>
          <li><a href="logs/">Raw file view</a><br/><small>(This will only work if the log files are stored in TOMCAT_HOME/webapps/citrine/logs/ i.e. the default as configured by the "sysexec.logpath" property in citrine.properties file.)</small></li>
        </ul>
        <%
        ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        UserManager userManager = (UserManager)context.getBean("userManager");
        
        User currentUser = userManager.getCurrentUser();
        if(currentUser.getRole().equals("admin"))
        {
        	%>
        	<li> <a href="admin.do">Admin</a> - administer Citrine instance.</li>
        	<%  	
        }        
        %>      
    </ul>
  </body>
</html>