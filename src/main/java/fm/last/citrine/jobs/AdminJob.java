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
package fm.last.citrine.jobs;

import static fm.last.citrine.scheduler.SchedulerConstants.SYS_ERR;
import static fm.last.citrine.scheduler.SchedulerConstants.SYS_OUT;
import static fm.last.citrine.scheduler.SchedulerConstants.TASK_COMMAND;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import fm.last.citrine.model.Task;
import fm.last.citrine.model.TaskRun;
import fm.last.citrine.service.LogFileManager;
import fm.last.citrine.service.TaskManager;
import fm.last.citrine.service.TaskRunManager;

/**
 * Job implementation for performing common Citrine admin tasks.
 */
public class AdminJob implements Job {

  private static Logger log = Logger.getLogger(AdminJob.class);

  public static final String COMMAND_CLEAR_TASK_RUNS = "clear_task_runs";
  public static final String COMMAND_CLEAR_LOG_FILES = "clear_log_files";  
  public static final String COMMAND_CHECK_JOB_RESOURCES = "check_job_resources";
  public static final String COMMAND_CLEAR_OLD_TASK_FOLDER = "clear_old_task_folder";

  private TaskRunManager taskRunManager;
  private LogFileManager logFileManager;
  private TaskManager taskManager;

 

@Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    String commandString = jobDataMap.getString(TASK_COMMAND);
    if (StringUtils.isEmpty(commandString)) {
      throw new JobExecutionException("Received empty admin command");
    }
    String[] command = commandString.split("\\s+");
    if (command.length != 2) {
      throw new JobExecutionException("Did not receive command and param");
    }
    String commandType = command[0];
    String argument = command[1];
    if (COMMAND_CLEAR_TASK_RUNS.equals(commandType)) {
      int days = Integer.parseInt(argument);
      DateTime deleteBefore = new DateTime().minusDays(days);
      taskRunManager.deleteBefore(deleteBefore);
      jobDataMap.put(SYS_OUT, "Deleted task runs on and before " + deleteBefore);
    } else if (COMMAND_CLEAR_LOG_FILES.equals(commandType)) {
      int days = Integer.parseInt(argument);
      DateTime deleteBefore = new DateTime().minusDays(days);
      try {
        logFileManager.deleteBefore(deleteBefore);
        jobDataMap.put(SYS_OUT, "Deleted log files older than " + deleteBefore);
      } catch (IOException e) {
        log.error("Error deleting log files", e);
        jobDataMap.put(SYS_ERR, "Error deleting log files " + e.getMessage());
      }
    } else if (COMMAND_CHECK_JOB_RESOURCES.equals(commandType)) {
        
    	List<JobExecutionContext> runningTasks = 
    			taskRunManager.getRunningTasks();
    	
    	for(JobExecutionContext ctx : runningTasks)
    	{
    		Long pid = (Long)ctx.getJobDetail().getJobDataMap().get("pid");
    		if (pid != null)
    		{
    			
    		}    		
    	}
     }
    else if (COMMAND_CLEAR_OLD_TASK_FOLDER.equals(commandType)) {
        
    	List<Task> taskList = taskManager.getTasks();
    	
    	for(Task task : taskList)
    	{
    		TaskRun taskRun = taskRunManager.getMostRecent(task.getId());
    		Date now = new Date();
    		if(now.getTime() > (taskRun.getEndDate().getTime() + 1000 * 86400))
    		{
    			try
    			{
	    			// pulisce le cartelle
	    			File workingDir = new File(task.getWorkingDirectory());
	
	    			File[] files = workingDir.listFiles();
	    			
	    			for(File file : files)
	    			{
	    				System.out.println("delete " + file.getAbsolutePath());
	    				
		    			try {
							Runtime.getRuntime().exec("rm -fr " + file.getAbsolutePath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    			}
	    			
	    			List<TaskRun> taskRunList = taskRunManager.findByTaskId(task.getId());
	    			
	    			for(TaskRun tr : taskRunList)
	    			{
	    				taskRunManager.delete(taskRun.getId());	
	    			}
	    			
	    			taskManager.delete(task);
    			}
    			catch(Exception ex)
    			{
    				ex.printStackTrace();
    			}
    		}
    	}
    	
     }else {
      throw new JobExecutionException("Invalid command type '" + commandType + "'");
    }
  }

  /**
   * @param taskRunManager the taskRunManager to set
   */
  public void setTaskRunManager(TaskRunManager taskRunManager) {
    this.taskRunManager = taskRunManager;
  }

  /**
   * @param logFileManager the logFileManager to set
   */
  public void setLogFileManager(LogFileManager logFileManager) {
    this.logFileManager = logFileManager;
  }
  
  public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public TaskRunManager getTaskRunManager() {
		return taskRunManager;
	}
  
}
