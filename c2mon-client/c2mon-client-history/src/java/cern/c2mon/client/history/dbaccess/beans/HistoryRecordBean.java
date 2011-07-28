/******************************************************************************
 * This file is part of the Technical Infrastructure Monitoring (TIM) project.
 * See http://ts-project-tim.web.cern.ch
 * 
 * Copyright (C) 2004 - 2011 CERN This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Author: TIM team, tim.support@cern.ch
 *****************************************************************************/
package cern.c2mon.client.history.dbaccess.beans;

import java.sql.Timestamp;
import java.util.Date;

import cern.tim.shared.common.datatag.DataTagQuality;

/**
 * When rows from the history tables are retrieved from the database, it is
 * converted into this object.
 * 
 * @author vdeila
 * 
 */
public class HistoryRecordBean {

  /** The tag id */
  private final Long tagId;
  
  /** The tag name */
  private String tagName;
  
  /** The tag value */
  private String tagValue;
  
  /** The tag type */
  private String tagDataType;
  
  /** The timestamp of when the equipment registered the event */
  private Timestamp tagTime;
  
  /** The timestamp of when the DAQ registered the event */
  private Timestamp daqTime;
  
  /** The timestamp of when the server registered the event */
  private Timestamp serverTime;
  
  /** The date of when the record was written into the database */
  private Date logDate;
  
  /** The quality descriptor */
  private DataTagQuality dataTagQuality;
  
  /** The tag mode */
  private short tagMode;
  
  /**
   * <code>true</code> if this records comes from the initial snapshot table. 
   * The log date will then zero hours, minutes and seconds
   */
  private boolean fromInitialSnapshot;
  
  /**
   * 
   * @param tagId The tag id this record will be registered for
   */
  public HistoryRecordBean(final Long tagId) {
    this.tagId = tagId;
  }
  
  /**
   * 
   * @return A string representation of the object
   */
  @Override
  public String toString() {
    String tagIdStr = "<null>";
    String tagTimeStr = "<null>";
    String logdateStr = "<null>";

    if (this.getTagId() != null) {
      tagIdStr = this.getTagId().toString();
    }
    if (this.getTagTime() != null) {
      tagTimeStr = this.getTagTime().toString();
    }
    if (this.getLogDate() != null) {
      logdateStr = this.getLogDate().toString();
    }
    
    return String.format("TagId: %s, timestamp: %s, logdate: %s",
        tagIdStr, tagTimeStr, logdateStr);
  }

  /**
   * @return the logDate
   */
  public Date getLogDate() {
    return logDate;
  }

  /**
   * @param logDate the logDate to set
   */
  public void setLogDate(final Date logDate) {
    this.logDate = logDate;
  }

  /**
   * @return the tagName
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * @param tagName the tagName to set
   */
  public void setTagName(final String tagName) {
    this.tagName = tagName;
  }

  /**
   * @return the tagValue
   */
  public String getTagValue() {
    return tagValue;
  }

  /**
   * @param tagValue the tagValue to set
   */
  public void setTagValue(final String tagValue) {
    this.tagValue = tagValue;
  }

  /**
   * @return the tagDataType
   */
  public String getTagDataType() {
    return tagDataType;
  }

  /**
   * @param tagDataType the tagDataType to set
   */
  public void setTagDataType(final String tagDataType) {
    this.tagDataType = tagDataType;
  }

  /**
   * @return the timestamp of when the equipment registered the event
   */
  public Timestamp getTagTime() {
    return tagTime;
  }

  /**
   * @param tagTime the timestamp to set of when the equipment registered the event
   */
  public void setTagTime(final Timestamp tagTime) {
    this.tagTime = tagTime;
  }

  /**
   * @return the tagMode
   */
  public short getTagMode() {
    return tagMode;
  }

  /**
   * @param tagMode the tagMode to set
   */
  public void setTagMode(final short tagMode) {
    this.tagMode = tagMode;
  }

  /**
   * @return the fromInitialSnapshot
   */
  public boolean isFromInitialSnapshot() {
    return fromInitialSnapshot;
  }

  /**
   * @param fromInitialSnapshot the fromInitialSnapshot to set
   */
  public void setFromInitialSnapshot(final boolean fromInitialSnapshot) {
    this.fromInitialSnapshot = fromInitialSnapshot;
  }

  /**
   * @return the tagId
   */
  public Long getTagId() {
    return tagId;
  }

  /**
   * @return the dataTagQuality
   */
  public DataTagQuality getDataTagQuality() {
    return dataTagQuality;
  }

  /**
   * @param dataTagQuality the dataTagQuality to set
   */
  public void setDataTagQuality(final DataTagQuality dataTagQuality) {
    this.dataTagQuality = dataTagQuality;
  }

  /**
   * @return the timestamp of when the DAQ registered the event
   */
  public Timestamp getDaqTime() {
    return daqTime;
  }

  /**
   * @param daqTime the timestamp to set of when the DAQ registered the event
   */
  public void setDaqTime(final Timestamp daqTime) {
    this.daqTime = daqTime;
  }

  /**
   * @return the timestamp of when the server registered the event
   */
  public Timestamp getServerTime() {
    return serverTime;
  }

  /**
   * @param serverTime the timestamp to set of when the server registered the event
   */
  public void setServerTime(final Timestamp serverTime) {
    this.serverTime = serverTime;
  }
  
  
  
}
