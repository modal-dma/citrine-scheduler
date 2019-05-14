<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<html>
  <head>
    <title>Create Task</title>
    <link rel="stylesheet" href="css/generic.css" type="text/css"/>
  </head>
  <body>
    <form method="POST" action="doTaskCreate.jsp" enctype = "multipart/form-data">      
      <table>
        <tr>
          <td align="right">* Name:</td>
          <td>
            <input name="name" size="30"/>
            <span class="error"><form:errors name="name.error" /></span>
          </td>
        </tr>
        <tr>
          <td align="right" valign="top">Description:</td>
          <td>
            <input type="text" name="description" rows="5" cols="80"/>
          </td>
        </tr>
        <tr>
          <td align="right" valign="top">Needed RAM (GB):</td>
          <td>
            <input type="text" name="ram" rows="5" cols="10"/>
          </td>
        </tr>
        <tr>
          <td align="right" valign="top">Number of needed cores:</td>
          <td>
            <input type="text" name="cores" rows="5" cols="5"/>
          </td>
        </tr>
        <tr>
          <td align="right">Schedule:</td>
          <td>
            <select name="timerSchedule">
			  <option value="1">Prima Possibile</option>
			  <option value="2">Attendi la coda</option>			  
			</select>           
          </td>
        </tr>
        <tr>
          <td align="right">Script File:</td>
          <td>
            <input type="file" name="scriptfile" size="80"/>
          </td>
        </tr>
        <tr>
          <td align="right">DataSet:</td>
          <td>
            <input type="file" name="dataset" size="80"/>
          </td>
        </tr>
        <tr>
          <td align="right">Command:</td>
          <td>
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
          <td align="right" valign="top">Notification Recipients:</td>
          <td>
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
          <td align="right">
            <input type="submit" value="Save and Start" name="update"/>
            <input type="submit" value="Cancel" name="cancel"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>