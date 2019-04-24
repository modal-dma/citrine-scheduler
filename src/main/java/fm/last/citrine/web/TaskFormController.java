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

import static fm.last.citrine.web.Constants.PARAM_CANCEL;
import static fm.last.citrine.web.Constants.PARAM_DELETE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import fm.last.citrine.model.Task;
import fm.last.citrine.service.TaskManager;
import fm.last.commons.io.FileUtils;

/**
 * Controller implementation for handling the form used for creating and editing Tasks.
 */
public class TaskFormController extends SimpleFormController {

  private static Logger log = Logger.getLogger(TaskFormController.class);

  private TaskManager taskManager;

  private static final String PARAM_ID = "id";

  private static final String DEFAULT_BEAN_NAME = "sysExecJob";

  /**
   * Bean name that will be set on new tasks (and therefore selected in view).
   */
  private String defaultBeanName = DEFAULT_BEAN_NAME;

  private List<String> getJobBeanNames() {
    String[] beanNames = getWebApplicationContext().getParent().getBeanNamesForType(Job.class);
    List<String> jobBeanNames = Arrays.asList(beanNames);
    return jobBeanNames;
  }

  private String getSuccessView(String selectedGroupName) {
    if (StringUtils.isEmpty(selectedGroupName)) {
      selectedGroupName = Constants.GROUP_NAME_ALL;
    }
    return "tasks.do?" + Constants.PARAM_SELECTED_GROUP_NAME + "=" + selectedGroupName;
  }

  @Override
  protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors)
    throws Exception {
    Map<String, Object> referenceData = new HashMap<String, Object>();
    referenceData.put("jobBeans", getJobBeanNames());

    String idString = request.getParameter(PARAM_ID);
    if (idString != null) { // only allow parent/child relationships to be created if Task is being edited, not created
      List<Task> groupTasks = taskManager.findTasksInSameGroup(((TaskDTO) command).getTask());
      referenceData.put("groupTasks", groupTasks);
    }

    referenceData.put(Constants.PARAM_SELECTED_GROUP_NAME, request.getParameter(Constants.PARAM_SELECTED_GROUP_NAME));
    return referenceData;
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws ServletException {
    String idString = request.getParameter(PARAM_ID);
    TaskDTO backingObject = new TaskDTO();
    if (request.getParameter(PARAM_CANCEL) == null && null != idString && !idString.equals("") && !idString.equals("0")) {
      long id = Long.parseLong(idString);
      backingObject.setTask(taskManager.get(id));
    } else { // creating a new one, set defaults
      backingObject.getTask().setBeanName(defaultBeanName);
    }

    String selectedGroupName = request.getParameter(Constants.PARAM_SELECTED_GROUP_NAME);
    if (selectedGroupName != null && !(Constants.GROUP_NAME_ALL.equals(selectedGroupName))) {
      backingObject.setSelectedGroupName(selectedGroupName);
    }        
    	    
    return backingObject;
  }

  public static byte[] getInputAsBinary(InputStream requestStream) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      try {
          byte[] buf = new byte[100000];
          int bytesRead=0;
          while ((bytesRead = requestStream.read(buf)) != -1){
          //while (requestStream.available() > 0) {
          //    int i = requestStream.read(buf);
              bos.write(buf, 0, bytesRead);
          }
          requestStream.close();
          bos.close();
      } catch (IOException e) {
      	e.printStackTrace();
          //Logger log = Logger.getLogger(HttpUtils.class.getName());
          //log.log(Level.SEVERE, "error while decoding http input stream", e);
      }
      return bos.toByteArray();
  }
  
  @Override
  public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command,
      BindException errors) throws Exception {
    if (request.getParameter(PARAM_CANCEL) != null) {
      TaskDTO taskDTO = (TaskDTO) command;
      return new ModelAndView(new RedirectView(getSuccessView(taskDTO.getSelectedGroupName())));
    }
    return super.processFormSubmission(request, response, command, errors);
  }

  @Override
  public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
      BindException errors) {
    TaskDTO taskDTO = (TaskDTO) command;
    Task oldTask = taskManager.get(taskDTO.getTask().getId());
    if (request.getParameter(PARAM_DELETE) != null) {
      // task from web doesn't have parent/child relationships, so retrieve it from db before deleting
      taskManager.delete(oldTask);
    } else {
      Task newTask = taskDTO.getTask();
      if (oldTask != null) {
        newTask.setChildTasks(oldTask.getChildTasks());
      }
      
      String contentType = request.getHeader("Content-Type");
      if(contentType.startsWith("multipart/form-data"))
      {
      	//found form data
          String boundary = contentType.substring(contentType.indexOf("boundary=")+9);
          // as of rfc7578 - prepend "\r\n--"
          byte[] boundaryBytes = ("--" + boundary).getBytes(Charset.forName("UTF-8"));
          
          System.out.println(boundaryBytes);
          
          //byte[] payload = getInputAsBinary(httpExchange.getRequestBody());
          //System.out.println(new String(payload));
          
          //ArrayList<MultiPart> list = new ArrayList<>();
      }
      	
      String workingDrectory = FileUtils.getWorkingDirectory("" + taskDTO.getTask().getId());   
      try
      {
      	InputStream ins = request.getInputStream();
      	
      	System.out.println(ins);
      	
      	ins.close();
      	
      }
      catch(IOException ex)
      {
      	ex.printStackTrace();
      }
      
      taskManager.save(newTask);
    }
    return new ModelAndView(new RedirectView(getSuccessView(taskDTO.getSelectedGroupName())));
  }

  public TaskManager getTaskManager() {
    return taskManager;
  }

  public void setTaskManager(TaskManager taskManager) {
    this.taskManager = taskManager;
  }

  /**
   * @return the defaultBeanName
   */
  public String getDefaultBeanName() {
    return defaultBeanName;
  }

  /**
   * @param defaultBeanName the defaultBeanName to set
   */
  public void setDefaultBeanName(String defaultBeanName) {
    this.defaultBeanName = defaultBeanName;
  }

}
