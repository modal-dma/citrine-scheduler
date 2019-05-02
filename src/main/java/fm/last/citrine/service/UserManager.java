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

import java.util.List;

import fm.last.citrine.model.User;

/**
 * Manages Tasks.
 */
public interface UserManager {

  /**
   * Retrieves a Task by its primary key.
   * 
   * @param id Task id.
   * @return Task identified by the passed id.
   */
  public User get(long id);

  /**
   * Creates a or updates a Task object in storage.
   * 
   * @param task Task to save.
   */
  public void save(User user);

  /**
   * Retrieves all Users.
   * 
   * @return List of all Users in storage.
   */
  public List<User> getUsers();
  
  /**
   * Retrieves the current user.
   * 
   * @return the current user.
   */
  public User getCurrentUser();
  
  public void setCurrentUser(User user);

  /**
   * Retrieves all Task belonging to the same group.
   * 
   * @param group Name of group.
   * @return Tasks which are in the passed group.
   */
  public List<User> findByUsername(String username);

  
  /**
   * Deletes a Task.
   * 
   * @param task Task to delete;
   */
//  public void delete(User user);

}
