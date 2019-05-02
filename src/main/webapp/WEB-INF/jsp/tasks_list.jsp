<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
  <head>
    <title>Tasks</title>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
    <meta http-equiv="refresh" content="30">
  </head>
  <body>
    <h2>Tasks Overview</h2>
    <p>
    <c:if test='${"admin" == currentUserRole}'>
     | Filter by user:
	    <select name="selectedGroupName" onChange="location='tasks.do?selectedGroupName=' + this.options[this.selectedIndex].value;">
	      <option value="-" <c:if test="${'-' == selectedGroupName}">selected</c:if>>-</option>
	      <c:forEach var="groupName" items="${groupNames}">
	        <option value="${groupName}" <c:if test="${groupName == selectedGroupName}">selected</c:if>>${groupName}</option>
	      </c:forEach>
	    </select> 
    </c:if>
    |
    <a href="taskcreate.jsp">Create New Task</a> |
 <!--    <a href="task_edit.do?selectedGroupName=${selectedGroupName}">Create New Task</a> | -->
    </p>
    <display:table name="${tasksExtended}" id="task" class="outlined" requestURI="tasks.do" defaultsort="2">
      <display:column title="Name" class="${task.status}" sortable="true">
        <c:if test='${currentUser == task.groupName}'>      
        	<a id="task-${task.id}" href="task_edit.do?id=${task.id}&selectedGroupName=${selectedGroupName}">${task.name}</a>        	
        </c:if>
        <c:if test='${currentUser ne task.groupName}'>      
        	${task.name}        	
        </c:if>
        
      </display:column>
      <display:column property="groupName" title="User" class="${task.status}" sortable="true"/>
      <display:column property="description" class="${task.status}"/>
      <display:column property="status" class="${task.status}"/>
      <display:column title="Task Runs" class="${task.status}">
      <c:if test='${currentUser == task.groupName}'>
        <a href="task_runs.do?action=list&taskId=${task.id}&selectedGroupName=${selectedGroupName}">Task Runs</a>
      </c:if>
      </display:column>
      <display:column title="Actions" class="${task.status}">
      	<c:if test='${currentUser == task.groupName}'>
	        <c:if test="${'running' ne recentStatus[task.id]}">
	          <c:if test='${"STARTED" == schedulerStatus}'>
	            <a href="tasks.do?action=run&taskId=${task.id}&selectedGroupName=${selectedGroupName}">Start</a>
	          </c:if>
	        </c:if>
        
	        <a href="tasks.do?action=reset&taskId=${task.id}&selectedGroupName=${selectedGroupName}">Reset</a>
	        <a href="download.jsp?taskId=${task.id}&selectedGroupName=${selectedGroupName}">Download</a>
	    </c:if>
      </display:column>
      <display:column title="last run" class="${task.status}">
      	<c:out value="${lastRun[task.id]}"></c:out>
      </display:column>
    </display:table>
    <p><a href="main.jsp">Back</a></p>
  </body>
</html>