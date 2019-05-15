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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Class that represents a Task. It contains values that are used to trigger Quartz Jobs at a certain time as well as
 * values that are needed by Tasks to run.
 */
@Entity
@Table(name = TableConstants.TABLE_SETTINGS)
public class Setting {

  private static Logger log = Logger.getLogger(Setting.class);

  private long id;
  private String key;
  private String val;

  public Setting(String key, String value) {
    this.key = key;
    this.val = value;
  }
  
  public Setting()
  {
	  
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(nullable = false)
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Column(length = 4000)
  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = val;
  }

  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((val == null) ? 0 : val.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Setting)) {
      return false;
    }
    final Setting other = (Setting) obj;
    if (key == null) {
      if (other.key!= null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }

    return true;
  }

  public String toString() {
    return "id=" + id + ",key=" + key + ",val=" + val;
  }

}
