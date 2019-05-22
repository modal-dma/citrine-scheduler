<%@page import="fm.last.util.SystemUtil"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.nio.file.attribute.FileAttribute"%>
<%@page import="java.nio.file.attribute.PosixFilePermissions"%>
<%@page import="java.nio.file.attribute.PosixFilePermission"%>
<%@page import="java.util.Set"%>
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

<%

String username = (String)getServletContext().getAttribute("username");
if(username == null)
{
	response.sendRedirect("login.jsp");
	return;
}

System.out.println("username " + username);
/*System.out.println(request.getParameterMap());

String name = request.getParameter("task.name");
String description = request.getParameter("task.description");
String timerSchedule = request.getParameter("task.timerSchedule");
String command = request.getParameter("task.command");
String priority = request.getParameter("task.priority");
String enabled = request.getParameter("task.enabled");
String errorIfRunning = request.getParameter("task.errorIfRunning");
String stopOnError = request.getParameter("task.stopOnError");
String recipients = request.getParameter("task.notification.recipients");
String notifyOnSuccess = request.getParameter("task.notifyOnSuccess");
String notifyOnFailure = request.getParameter("task.notifyOnFailure");
*/

ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
TaskManager taskManager = (TaskManager)context.getBean("taskManager");
/*
Task task = new Task(name, username, "sysExecJob", enabled.equals("true"), stopOnError.equals("true"), command, timerSchedule);
task.setDescription(description);
task.setErrorIfRunning(errorIfRunning.equals("true"));
task.setPriority(Integer.parseInt(priority));
*/

String uuid = "" + System.currentTimeMillis();

String baseWorkingDir = "/var/tmp/jobscheduler/workspace";
File workingDirectory = new File(baseWorkingDir, username.replace(" ", "_") + File.separator + uuid);
if(!workingDirectory.exists())
	if(!workingDirectory.mkdirs())
	{
		System.out.println("unable to mkdirs " + workingDirectory.getAbsolutePath());
	}

//task.setWorkingDirectory(workingDirectory.getAbsolutePath());

int maxFileSize = 5000000 * 1024;
int maxMemSize = 5000 * 1024;

DiskFileItemFactory factory = new DiskFileItemFactory();
// maximum size that will be stored in memory
factory.setSizeThreshold(maxMemSize);

// Location to save data that is larger than maxMemSize.
factory.setRepository(workingDirectory);

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);

// maximum file size to be uploaded.
upload.setSizeMax( maxFileSize );


try {
	
	Task task = new Task("", username, "sysExecJob");	
	
	task.setWorkingDirectory(workingDirectory.getAbsolutePath());
	task.setUuid(uuid);
	
   // Parse the request to get file items.
   List<FileItem> fileItems = upload.parseRequest(request);

   // Process the uploaded file items
   Iterator<FileItem> i = fileItems.iterator();
   
   String notifyOnSuccess;
   String notifyOnFailure;
   Notification notification = new Notification();
	
   while ( i.hasNext () ) {
      FileItem fi = (FileItem)i.next();
      String fieldName = fi.getFieldName();
      System.out.println(fieldName);
      if ( !fi.isFormField () ) 
      {
         // Get the uploaded file parameters
         
         String fileName = fi.getName().trim();;
         boolean isInMemory = fi.isInMemory();
         long sizeInBytes = fi.getSize();
      
         File file;
         
         if(!fileName.isEmpty())
         {
	         // Write the file
	         if( fileName.lastIndexOf("\\") >= 0 ) {
	            file = new File( workingDirectory, 
	            fileName.substring( fileName.lastIndexOf("\\"))) ;
	         } else {
	            file = new File( workingDirectory, 
	            fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	         }
	         
	         fi.write( file ) ;
	         
	         if(fieldName.equals("zipfile"))
	         {
	        	 SystemUtil.unpack(file.getAbsolutePath(), workingDirectory.getAbsolutePath());
	         }
	         
	         file.delete();
	         
// 	        	 if(file.getAbsolutePath().endsWith(".sh"))
// 	        	 {
// 	        		 getServletContext().setAttribute("error", "Gli script .sh non sono consentiti.");
// 	        		 response.sendRedirect("error.jsp");
// 	        		 return;
// 	        	 }
// 	        	 else
// 	        	 {
// 	        	 	task.setScriptfile(file.getAbsolutePath());
// 	        	 }
	        	 
// 	        	 Set<PosixFilePermission> perms = new HashSet();
// 	       	    perms.add(PosixFilePermission.OWNER_READ);
// 	       	    perms.add(PosixFilePermission.OWNER_WRITE);
// 	       	    perms.add(PosixFilePermission.OWNER_EXECUTE);
	
// 	       	    perms.add(PosixFilePermission.OTHERS_READ);
// 	       	    perms.add(PosixFilePermission.OTHERS_WRITE);
// 	       	    perms.add(PosixFilePermission.OTHERS_EXECUTE);
	
// 	       	    perms.add(PosixFilePermission.GROUP_READ);
// 	       	    perms.add(PosixFilePermission.GROUP_WRITE);
// 	       	    perms.add(PosixFilePermission.GROUP_EXECUTE);
	
// 	       	    Files.setPosixFilePermissions(file.toPath(), perms);
	        	    
// 	      		 file.setWritable(true);
// 	      		 file.setReadable(true);
// 	      		 file.setExecutable(true);        		 
// 	         }
// 	         else if(fieldName.equals("dataset"))
// 	         {
// 	        	 task.setDataset(file.getAbsolutePath());
// 	        	 file.setWritable(true);
// 	       		 file.setReadable(true);
// 	         }
         }
         
      }
      else
      {
          String value = fi.getString();
    	  
    	  if(fieldName.equals("name"))    	
    		  task.setName(value);    	 
    	  else if(fieldName.equals("description"))
    		  task.setDescription(value);
//     	  else if(fieldName.equals("priority"))
//     		  task.setPriority(Integer.parseInt(value));
    	  else if(fieldName.equals("timerSchedule"))
    		  task.setTimerSchedule(value);
    	  else if(fieldName.equals("command"))
    		  task.setCommand(value);
    	  else if(fieldName.equals("schedule"))
    		  task.setTimerSchedule(value);
//     	  else if(fieldName.equals("enabled"))
//     		  task.setEnabled(value.equals("true"));
//     	  else if(fieldName.equals("errorIfRunning"))
//     		  task.setErrorIfRunning(value.equals("true"));
//     	  else if(fieldName.equals("stopOnError"))
//     		  task.setStopOnError(value.equals("true"));
//     	  else if(fieldName.equals("notifyOnSuccess"))
//     		  notification.setNotifyOnSuccess(value.equals("true"));
//     	  else if(fieldName.equals("notifyOnFailure"))
//     		  notification.setNotifyOnFailure(value.equals("true"));
    	  else if(fieldName.equals("notification.recipients"))
    		  notification.setRecipients(value);    	      	
    	  else if(fieldName.equals("ram"))    	
    	  {
    		  if("".equals(value))
    		  {
    			  getServletContext().setAttribute("error", "Valore della RAM richiesta mancante");
    			   response.sendRedirect("error.jsp");
    			   return;
    		  }
    		  
    		  task.setRam(Integer.parseInt(value));
    	  }
    	  else if(fieldName.equals("cores"))
    	  {
    		  if("".equals(value))
    		  {
    			  getServletContext().setAttribute("error", "Valore dei core richiesti mancante");
    			   response.sendRedirect("error.jsp");
    			   return;
    		  }
    		  
    		  task.setCores(Integer.parseInt(value));
    	  }    		      	    
      }            
   }
   
   if(task.getName() == null || task.getName().equals(""))
   {
	   getServletContext().setAttribute("error", "Nome del task mancante");
	   response.sendRedirect("error.jsp");
	   return;
   }
   
   	if(task.getCommand() == null || task.getCommand().equals(""))
	{
   		getServletContext().setAttribute("error", "Comando del task mancante");
 	   response.sendRedirect("error.jsp");
 	   return;
	}
   
//    if(task.getScriptfile() == null || task.getScriptfile().equals(""))
//    {
// 	   getServletContext().setAttribute("error", "Script file del task mancante");
// 	   response.sendRedirect("error.jsp");
// 	   return;
//    }
   
   task.setNotification(notification);
   task.setEnabled(true);
   task.setErrorIfRunning(true);
   task.setStopOnError(false);
   notification.setNotifyOnSuccess(true);
   notification.setNotifyOnFailure(true);
   taskManager.save(task);
   
   response.sendRedirect("tasks.do");
} catch(Exception ex) {
   System.out.println(ex);
   
   getServletContext().setAttribute("error", ex.getLocalizedMessage());
   response.sendRedirect("error.jsp");
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