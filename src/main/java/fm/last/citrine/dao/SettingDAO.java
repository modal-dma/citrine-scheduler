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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import fm.last.citrine.model.Setting;

/**
 * Data Access Object for managing storage of Tasks.
 */
public class SettingDAO extends HibernateDaoSupport {

  private static Logger log = Logger.getLogger(SettingDAO.class);

  /**
   * Creates a or updates a Task object in storage.
   * 
   * @param task Task to save.
   */
  public void save(Setting setting) {
    getHibernateTemplate().saveOrUpdate(setting);
    getHibernateTemplate().flush();
  }

  /**
   * Retrieves a Setting by its primary key.
   * 
   * @param id Task id.
   * @return Task identified by the passed id.
   */
  public Setting get(long id) {
    return (Setting) getHibernateTemplate().get(Setting.class, id);
  }

  /**
   * Retrieves a Setting by its Settingname.
   * 
   * @param Settingname Settingname.
   * @return Setting identified by the passed Settingname.
   */
  public List<Setting> get(String key) {
    return getHibernateTemplate().findByNamedParam("from Setting where key = :key", "key", key);
  }

  /**
   * Retrieves all Tasks in storage.
   * 
   * @return List of all Tasks in storage.
   */
  public List<Setting> getSettings() {
    return getHibernateTemplate().find("from Setting order by key asc");
  }
}
