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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import fm.last.citrine.dao.SettingDAO;
import fm.last.citrine.dao.UserDAO;
import fm.last.citrine.model.Setting;

/**
 * Manages Settings.
 */
public class SettingManagerImpl implements SettingManager, BeanFactoryAware {

	private SettingDAO settingDAO;
	  
  public SettingDAO getSettingDAO() {
		return settingDAO;
	}

	public void setSettingDAO(SettingDAO settingDAO) {
		this.settingDAO = settingDAO;
	}

/**
   * Retrieves a Setting by its primary key.
   * 
   * @param id Setting id.
   * @return Setting identified by the passed id.
   */
  public Setting get(long id)
  {
	  return settingDAO.get(id);
  }

  /**
   * Retrieves a Setting by its primary key.
   * 
   * @param key Setting key.
   * @return Setting identified by the passed key.
   */
  public Setting get(String key)
  {
	  List<Setting> settingList = settingDAO.get(key);
	  
	  if(settingList.size() > 0)
		  return settingList.get(0);
	  else
		  return null;
  }
  
  /**
   * Creates a or updates a Setting object in storage.
   * 
   * @param Setting Setting to save.
   */
  public void save(Setting setting)
  {
	  
  }

  /**
   * Retrieves all Users.
   * 
   * @return List of all Users in storage.
   */
  public List<Setting> getSettings()
  {
	  return null;
  }
  
  /**
   * Retrieves all Setting belonging to the same group.
   * 
   * @param group Name of group.
   * @return Settings which are in the passed group.
   */
  public List<Setting> findByKey(String key)
  {
	  return null;
  }

  
  /**
   * Deletes a Setting.
   * 
   * @param Setting Setting to delete;
   */
//  public void delete(Setting setting);

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    // for some bizarre reason setBeanFactory gets called on this class *before* it gets called on
    // schedulerManager, and we need it set on schedulerManager during init-method, set set it now
    //schedulerManager.setBeanFactory(beanFactory);
  }
  
}
