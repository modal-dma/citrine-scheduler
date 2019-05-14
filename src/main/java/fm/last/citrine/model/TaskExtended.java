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
package fm.last.citrine.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;
import org.hibernate.annotations.AccessType;

/**
 * Class that represents a Task. It contains values that are used to trigger Quartz Jobs at a certain time as well as
 * values that are needed by Tasks to run.
 */
public class TaskExtended extends Task {

  private static Logger log = Logger.getLogger(TaskExtended.class);

  private Status status;
  private String lastRun;
  

  public TaskExtended(Task task, Status status, String lastRun)
  {
	  super(task);
	  this.setStatus(status);	  
	  this.setLastRun(lastRun);
  }


public Status getStatus() {
	return status;
}


public void setStatus(Status status) {
	this.status = status;
}


public String getLastRun() {
	return lastRun;
}


public void setLastRun(String lastRun) {
	this.lastRun = lastRun;
}

 
}
