<%@page import="fm.last.citrine.service.UserManager"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="fm.last.citrine.model.User"%>
<%@page import="java.util.List"%>
<%@page import="fm.last.citrine.dao.UserDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%

String username = request.getParameter("username");
String password = request.getParameter("password");

ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
UserManager userManager = (UserManager)context.getBean("userManager");

List<User> users = userManager.findByUsername(username);

if(users.size() == 0)
{
	getServletContext().setAttribute("error", "User not found.");
	response.sendRedirect("error.jsp");
	return;
}
else
{
	User user = users.get(0);
	if(!user.getPassword().equals(password))
	{
		getServletContext().setAttribute("error", "Wrong password.");
		response.sendRedirect("error.jsp");
		return;
	}
	else
	{
		userManager.setCurrentUser(user);
		getServletContext().setAttribute("username", username);
		if(user.getRole().equals("admin"))
			response.sendRedirect("main.jsp");
		else
			response.sendRedirect("tasks.do");
	}	
}





%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<f:view>

</f:view>
</body>
</html>