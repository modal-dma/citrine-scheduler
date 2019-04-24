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
          <td align="right">Timer Schedule<span class="reference"><a href="http://quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger" target="_blank">[?]</a></span>:</td>
          <td>
            <input name="timerSchedule"/>
            <span class="error"><form:errors name="timerSchedule.error" /></span>
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
        <tr>
          <td align="right">Priority:</td>
          <td>
            <input name="priority"/>
            <span class="error"><form:errors path="priority.error" /></span>
          </td>
        </tr>
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
        <tr>
          <td align="right" valign="top">Notification Recipients:</td>
          <td>
            <input name="notification.recipients" size="80"/><br/> 
            (comma-separated list of e-mail addresses)
          </td>
        </tr>
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
        <tr>
          <td align="right">
            <input type="submit" value="Save" name="update"/>
            <input type="submit" value="Cancel" name="cancel"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>