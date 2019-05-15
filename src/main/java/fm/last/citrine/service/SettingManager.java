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

import fm.last.citrine.model.Setting;

/**
 * Manages Settings.
 */
public interface SettingManager {

  /**
   * Retrieves a Setting by its primary key.
   * 
   * @param id Setting id.
   * @return Setting identified by the passed id.
   */
  public Setting get(long id);

  /**
   * Retrieves a Setting by its primary key.
   * 
   * @param key Setting key.
   * @return Setting identified by the passed key.
   */
  public Setting get(String key);
  
  /**
   * Creates a or updates a Setting object in storage.
   * 
   * @param Setting Setting to save.
   */
  public void save(Setting setting);

  /**
   * Retrieves all Users.
   * 
   * @return List of all Users in storage.
   */
  public List<Setting> getSettings();
  
  /**
   * Retrieves all Setting belonging to the same group.
   * 
   * @param group Name of group.
   * @return Settings which are in the passed group.
   */
  public List<Setting> findByKey(String key);

  
  /**
   * Deletes a Setting.
   * 
   * @param Setting Setting to delete;
   */
//  public void delete(Setting setting);

}
