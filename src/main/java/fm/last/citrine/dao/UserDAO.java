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
package fm.last.citrine.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import fm.last.citrine.model.Task;
import fm.last.citrine.model.User;

/**
 * Data Access Object for managing storage of Tasks.
 */
public class UserDAO extends HibernateDaoSupport {

  private static Logger log = Logger.getLogger(UserDAO.class);

  /**
   * Creates a or updates a Task object in storage.
   * 
   * @param task Task to save.
   */
  public void save(User user) {
    getHibernateTemplate().saveOrUpdate(user);
    getHibernateTemplate().flush();
  }

  /**
   * Retrieves a User by its primary key.
   * 
   * @param id Task id.
   * @return Task identified by the passed id.
   */
  public User get(long id) {
    return (User) getHibernateTemplate().get(User.class, id);
  }

  /**
   * Retrieves a User by its username.
   * 
   * @param Username Username.
   * @return User identified by the passed username.
   */
  public List<User> get(String username) {
    return getHibernateTemplate().findByNamedParam("from User where username = :username", "username", username);
  }

  /**
   * Retrieves all Tasks in storage.
   * 
   * @return List of all Tasks in storage.
   */
  public List<User> getUsers() {
    return getHibernateTemplate().find("from user order by username asc");
  }

  /**
   * Retrieves all Tasks belonging to the same group.
   * 
   * @param group Name of group.
   * @return Tasks which are in the passed group.
   */
  public List<User> findByRole(String role) {
    return getHibernateTemplate().findByNamedParam("from User where role = :role", "role", role);
  }
}
