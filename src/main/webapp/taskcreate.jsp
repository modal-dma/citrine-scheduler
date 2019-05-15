<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<html>
  <head>
    <title>Create Task</title>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
  </head>
  <body>
  <%@include file="header.jsp" %>
    <form method="POST" action="doTaskCreate.jsp" enctype = "multipart/form-data">      
      <table>
        <tr>
          <td class="td-desc" align="right">* Name:</td>
          <td class="td-value">
            <input name="name" size="80"/>
            <span class="error"><form:errors name="name.error" /></span>
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right" valign="top">Description:</td>
          <td class="td-value">
            <input type="text" name="description" size="80"/>
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right" valign="top">Needed RAM (GB):</td>
          <td class="td-value">
            <input type="text" name="ram" size="3"/>
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right" valign="top">Number of needed cores:</td>
          <td class="td-value">
            <input type="text" name="cores" size="3"/>
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right">Schedule:</td>
          <td class="td-value">
            <select name="timerSchedule">
			  <option value="1">Prima Possibile</option>
			  <option value="2">Attendi la coda</option>			  
			</select>           
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right">Script File:</td>
          <td class="td-value">
            <input type="file" name="scriptfile" size="80"/>
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right">DataSet:</td>
          <td class="td-value">
            <input type="file" name="dataset" size="80"/>
          </td>
        </tr>
        <tr>
          <td class="td-desc" align="right">Command:</td>
          <td class="td-value">
            <input name="command" size="80"/>
          </td>
        </tr>        
        <!-- 
        <tr>
          <td align="right">Enabled:</td>
          <td>
            <input type="checkbox" name="enabled"/>
          </td>
        </tr>
        <tr>
          <td align="right">Stop On Error:</td>
          <td>
            <input type="checkbox" name="stopOnError"/>
          </td>
        </tr>
        <tr>
          <td align="right">Error If Running:</td>
          <td>
            <input type="checkbox" name="errorIfRunning"/>
          </td>
        </tr>
         -->
        <tr>
          <td class="td-desc" align="right" valign="top">Notification Recipients:</td>
          <td class="td-value">
            <input name="notification.recipients" size="80"/><br/> 
            (comma-separated list of e-mail addresses)
          </td>
        </tr>
        <!-- 
        <tr>
          <td align="right">Notify on success:</td>
          <td>
            <input type="checkbox" name="notification.notifyOnSuccess"/>
          </td>
        </tr>
        <tr>
          <td align="right">Notify on failure:</td>
          <td>
            <input type="checkbox" name="notification.notifyOnFailure"/>
          </td>
        </tr>
         -->
        <tr>
        <td class="td-desc">
        &nbsp;
        </td>
          <td class="td-value" align="right">
            <input type="submit" value="Save and Start" name="update"/>
            <input type="button" value="Cancel" name="cancel" onclick="window.history.back();"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>