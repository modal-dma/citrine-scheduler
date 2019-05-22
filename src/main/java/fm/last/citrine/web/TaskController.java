/*
 * Copyright 2010 Last.fm
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package fm.last.citrine.web;

import static fm.last.citrine.web.Constants.PARAM_TASK_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import fm.last.citrine.model.Status;
import fm.last.citrine.model.Task;
import fm.last.citrine.model.TaskExtended;
import fm.last.citrine.model.TaskRun;
import fm.last.citrine.scheduler.SchedulerManager;
import fm.last.citrine.service.TaskManager;
import fm.last.citrine.service.TaskRunManager;
import fm.last.citrine.service.UserManager;

/**
 * Controller that handles listing and running of Tasks.
 */
public class TaskController extends MultiActionController {

  static final String TASK_STATUS_DISABLED = "disabled";

  private static Logger log = Logger.getLogger(TaskController.class);

  private TaskManager taskManager;
  private TaskRunManager taskRunManager;

  private SchedulerManager schedulerManager;

  private LastRunPeriodFormatter periodFormatter = new LastRunPeriodFormatter();

  /**
   * Fetches a List of tasks and adds group-related entries to the model based on the passed group name.
   * 
   * @param selectedGroupName Currently selected group name.
   * @param model Model to add group-related entries to.
   * @return A List of Tasks related to the passed selected group name.
   */
  private List<Task> handleGroupNames(String selectedGroupName, Map<String, Object> model) {
    List<Task> tasks = null;
    Set<String> groupNames = new TreeSet<String>();
    if (StringUtils.isEmpty(selectedGroupName) || (Constants.GROUP_NAME_ALL.equals(selectedGroupName))) {
      tasks = taskManager.getTasks();
      // no need to query for group names as we have all tasks, so just iterate thru them
      for (Task task : tasks) {
        String groupName = task.getGroupName();
        if (groupName != null) {
          groupNames.add(groupName);
        }
      }
      selectedGroupName = Constants.GROUP_NAME_ALL;
    } else {
      // we only have selected group name, need to get rest of them for dropdown
      groupNames = taskManager.getGroupNames();
      if (groupNames.contains(selectedGroupName)) {
        tasks = taskManager.findByGroup(selectedGroupName);
      } else { // selected group no longer exists, revert to all
        selectedGroupName = Constants.GROUP_NAME_ALL;
        tasks = taskManager.getTasks();
      }
    }
    model.put(Constants.PARAM_SELECTED_GROUP_NAME, selectedGroupName);
    model.put("groupNames", groupNames);
    return tasks;
  }

  /**
   * Performs processing on the passed Tasks in order to prepare them for viewing.
   * 
   * @param tasks List of tasks to process.
   * @param model Model to add task-related entries to.
   */
  private void processTasks(List<Task> tasks, Map<String, Object> model) {
    Map<Long, String> taskRunStatus = new HashMap<Long, String>();
    Map<Long, String> lastRun = new HashMap<Long, String>();
    ArrayList<TaskExtended> tasksExtended = new ArrayList<TaskExtended>();
    
    for (Task task : tasks) {

    	if(task.getGroupName().equals("admin") && !taskManager.getUserManager().getCurrentUser().getRole().equals("admin"))
    	{
    		continue;
    	}
    	
      // limit the description text based in the gui (we could also do this via displaytag)
      String description = task.getDescription();
      if (description != null) {
        int periodIndex = description.indexOf(".");
        if (periodIndex > 0) { // only display up to first "."
          task.setDescription(description.substring(0, periodIndex));
        }
      }
         
      // get the most recent status for each task
      TaskRun mostRecentTaskRun = taskRunManager.getMostRecent(task.getId());
      if (task.isEnabled()) {
        if (mostRecentTaskRun != null && mostRecentTaskRun.getStatus() != null) {
          taskRunStatus.put(task.getId(), mostRecentTaskRun.getStatus().toString().toLowerCase());
        } else {
          taskRunStatus.put(task.getId(), Status.UNKNOWN.toString().toLowerCase());
        }
      } else {
        taskRunStatus.put(task.getId(), TASK_STATUS_DISABLED);
      }
      lastRun.put(task.getId(), periodFormatter.printLastRunDate(mostRecentTaskRun));
      
      TaskExtended taskExtended;
      if(mostRecentTaskRun != null)
      {
	      taskExtended = new TaskExtended(task, mostRecentTaskRun.getStatus(), periodFormatter.printLastRunDate(mostRecentTaskRun));
	      tasksExtended.add(taskExtended);
	      
	      if(mostRecentTaskRun.getStatus() == Status.RUNNING)
	      {
	    	  Date startDate = mostRecentTaskRun.getStartDate();
	    	  
	    	  Date now = new Date();
	    	  
	    	  taskExtended.setDuration(now.getTime() - startDate.getTime());  
	    	  taskExtended.setDurationString(periodFormatter.printLastRun(mostRecentTaskRun));
	      }	      
      }
      else
      {
    	  taskExtended = new TaskExtended(task, Status.UNKNOWN, periodFormatter.printLastRunDate(mostRecentTaskRun));
	      tasksExtended.add(taskExtended);
      }      
      
      
    }
    
    Collections.sort(tasksExtended, new Comparator<TaskExtended>() {

		@Override
		public int compare(TaskExtended o1, TaskExtended o2) {
			
			return o2.getLastRun().compareTo(o1.getLastRun());
		}
	});
	
    model.put("lastRun", lastRun);
    model.put("recentStatus", taskRunStatus);
    model.put("tasks", tasks);
    model.put("tasksExtended", tasksExtended);
    model.put("currentUser", taskManager.getUserManager().getCurrentUser().getUsername());
    model.put("currentUserRole", taskManager.getUserManager().getCurrentUser().getRole());
  }

  /**
   * Lists tasks.
   * 
   * @param request
   * @param response
   * @return A ModelAndView to render.
   * @throws Exception
   */
  public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {

	  String username = (String)getServletContext().getAttribute("username");
	  if(username == null)
	  {
		  return new ModelAndView(new RedirectView("index.jsp"));
	  }
    
	  Map<String, Object> model = new HashMap<String, Object>();
	  model.put("schedulerStatus", schedulerManager.getStatus());
	  List<Task> tasks = handleGroupNames(request.getParameter(Constants.PARAM_SELECTED_GROUP_NAME), model);
	  processTasks(tasks, model);
	  return new ModelAndView("tasks_list", model);
  }

  /**
   * Handles a request to run a particular task.
   * 
   * @param request
   * @param response
   * @return A ModelAndView to render.
   * @throws Exception
   */
  public ModelAndView run(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String username = (String)getServletContext().getAttribute("username");
	  if(username == null)
	  {
		  return new ModelAndView(new RedirectView("index.jsp"));
	  }
	  
    long taskId = RequestUtils.getLongValue(request, PARAM_TASK_ID);
    log.debug("Received request to run task " + taskId);
    Task task = taskManager.get(taskId);
    schedulerManager.runTaskNow(task);
    Thread.sleep(1000); // bit of a hack, but allows TaskRun to be created so view should contain it
    // total hack to have path to .do here, but unsure how else to redirect there
    return new ModelAndView(new RedirectView("task_runs.do?action=list&" + Constants.PARAM_TASK_ID + "=" + taskId + "&"
        + Constants.PARAM_SELECTED_GROUP_NAME + "=" + request.getParameter(Constants.PARAM_SELECTED_GROUP_NAME)));
  }

  /**
   * Handles a request to reset a particular task.
   * 
   * @param request
   * @param response
   * @return A ModelAndView to render.
   * @throws Exception
   */
  public ModelAndView reset(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String username = (String)getServletContext().getAttribute("username");
	  if(username == null)
	  {
		  return new ModelAndView(new RedirectView("index.jsp"));
	  }
	  
    long taskId = RequestUtils.getLongValue(request, PARAM_TASK_ID);
    log.debug("Received request to reset task " + taskId);
    Task task = taskManager.get(taskId);
    schedulerManager.resetTask(task);
    return new ModelAndView(new RedirectView("tasks.do?" + Constants.PARAM_SELECTED_GROUP_NAME + "="
        + request.getParameter(Constants.PARAM_SELECTED_GROUP_NAME)));
  }

  public TaskManager getTaskManager() {
    return taskManager;
  }

  public void setTaskManager(TaskManager taskManager) {
    this.taskManager = taskManager;
  }

  public SchedulerManager getSchedulerManager() {
    return schedulerManager;
  }

  public void setSchedulerManager(SchedulerManager schedulerManager) {
    this.schedulerManager = schedulerManager;
  }

  public TaskRunManager getTaskRunManager() {
    return taskRunManager;
  }

  public void setTaskRunManager(TaskRunManager jobRunManager) {
    this.taskRunManager = jobRunManager;
  }
}
