<%@page import="fm.last.util.SystemUtil"%>
<%@page import="java.util.zip.ZipEntry"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.util.zip.ZipOutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@page import="fm.last.citrine.model.Notification"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.io.File"%>
<%@page import="fm.last.citrine.service.TaskManager"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="fm.last.citrine.model.Task"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%

String username = (String)getServletContext().getAttribute("username");
if(username == null)
{
	response.sendRedirect("login.jsp");
	return;
}

String taskid = request.getParameter("taskId");
if(taskid == null)
{
	response.sendError(401, "Task not found");
	return;
}


System.out.println("username " + username);
System.out.println("task id " + taskid);

ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
TaskManager taskManager = (TaskManager)context.getBean("taskManager");

Task task = taskManager.get(Long.parseLong(taskid));
if(task == null)
{
	response.sendError(401, "Task not found");
	return;
}

String workingDir = task.getWorkingDirectory();

System.out.println(workingDir);

File zipFile = File.createTempFile("task", ".zip");

SystemUtil.pack(workingDir, zipFile.getAbsolutePath());
String uuid = "" + System.currentTimeMillis();
String relativeWebPath = "/workspace";
String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
File webWorkingDirectory = new File(absoluteDiskPath, task.getGroupName() + "/" + task.getUuid());
if(!webWorkingDirectory.exists())
	webWorkingDirectory.mkdirs();

Process p = Runtime.getRuntime().exec("mv " + zipFile.getAbsolutePath() + " " + webWorkingDirectory);

p.waitFor();

response.sendRedirect("workspace/" + task.getGroupName() + "/" + task.getUuid() + "/" + zipFile.getName());

%>

</body>
</html>