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
package fm.last.citrine.service;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import fm.last.citrine.dao.UserDAO;
import fm.last.citrine.model.User;

/**
 * TaskManager implementation.
 */
public class UserManagerImpl implements UserManager, BeanFactoryAware {

  private UserDAO userDAO;
  private User currentUser;
  

	public UserManagerImpl() {
	}

 
  /**
   * Retrieves a Task by its primary key.
   * 
   * @param id Task id.
   * @return Task identified by the passed id.
   */
  @Override
  public User get(long id) {
    return userDAO.get(id);
  }

  /**
   * Creates a or updates a Task object in storage.
   * 
   * @param task Task to save.
   */
  @Override
  public void save(User user) {
    userDAO.save(user);    
  }

  /**
   * Retrieves all Tasks in storage.
   * 
   * @return List of all Tasks in storage.
   */
  @Override
  public List<User> getUsers() {
    return userDAO.getUsers();
  }

  @Override
  public List<User> findByUsername(String username) {
    return userDAO.get(username);
  }

  public UserDAO getUserDAO() {
		return userDAO;
	}
  
  public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

  /**
   * Deletes a Task.
   * 
   * @param task Task to delete;
   */
//  @Override
//  public void delete(Task task) {
//    schedulerManager.unscheduleTask(task);
//    taskRunDAO.deleteByTaskId(task.getId()); // manually manage this association
//    taskDAO.delete(task);
//  }


  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    // for some bizarre reason setBeanFactory gets called on this class *before* it gets called on
    // schedulerManager, and we need it set on schedulerManager during init-method, set set it now
    //schedulerManager.setBeanFactory(beanFactory);
  }


	@Override
	public User getCurrentUser() {
		
		return currentUser;
	}
	
	
	@Override
	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
