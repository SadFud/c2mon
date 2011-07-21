/******************************************************************************
 * This file is part of the Technical Infrastructure Monitoring (TIM) project.
 * See http://ts-project-tim.web.cern.ch
 * 
 * Copyright (C) 2005-2011 CERN.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Author: TIM team, tim.support@cern.ch
 *****************************************************************************/
package cern.c2mon.server.shorttermlog.mapper;

import java.sql.Time;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cern.c2mon.server.shorttermlog.mapper.DataTagLogMapper;
import cern.c2mon.server.shorttermlog.structure.TagShortTermLog;

/**
 * Tests the iBatis mapper against the Oracle DB.
 * 
 * @author Mark Brightwell
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cern/c2mon/server/shorttermlog/config/server-shorttermlog-test.xml"})
public class DataTagLogMapperTest {

  /**
   * Test data tag fields.
   */
  private static final Long ID = 10L;
  private static final String DATATYPE = "Integer";
  private static final String TAGVALUE = "25";
  private static final java.sql.Timestamp TAGTIME = new java.sql.Timestamp(System.currentTimeMillis()); 
  private static final java.sql.Timestamp LOGTIME = new java.sql.Timestamp(System.currentTimeMillis()+1000); 
    
  /**
   * To test.
   */
  @Autowired
  private DataTagLogMapper dataTagLogMapper;    
  
  /**
   * Removes test values from previous tests in case clean up failed.
   */
  @Before
  public void beforeTest() {
    removeTestData();
  }
  
  /**
   * Removes test values after test.
   */
  @After
  public void afterTest() {
    removeTestData();
  }
  
  /**
   * Removes test data.
   */
  private void removeTestData() {
    dataTagLogMapper.deleteDataTagLog(ID);
  }
  
  /**
   * Tests insertion completes successfully when fallback not
   * active (so no logtime set in object).
   */
  @Test
  public void testInsertDataTagLog() {
    TagShortTermLog dataTagShortTermLog = new TagShortTermLog();
    dataTagShortTermLog.setTagId(ID);
    dataTagShortTermLog.setTagDataType(DATATYPE);    
    dataTagShortTermLog.setSourceTimestamp(TAGTIME); 
    dataTagShortTermLog.setDaqTimestamp(TAGTIME);
    dataTagShortTermLog.setServerTimestamp(TAGTIME);    
    dataTagShortTermLog.setTagValue(TAGVALUE);
    dataTagLogMapper.insertLog(dataTagShortTermLog);
  }
  
  /**
   * Tests insertion completes successfully when fallback is
   * active. In this case, the logtime needs including as does
   * a specification of the timezone of the DB.
   */
  @Test
  public void testInsertDataTagLogFromFallback() {
    TagShortTermLog dataTagShortTermLog = new TagShortTermLog();
    dataTagShortTermLog.setTagId(ID);
    dataTagShortTermLog.setTagDataType(DATATYPE);
    dataTagShortTermLog.setLogDate(LOGTIME);
    dataTagShortTermLog.setSourceTimestamp(TAGTIME); 
    dataTagShortTermLog.setDaqTimestamp(TAGTIME); 
    dataTagShortTermLog.setServerTimestamp(TAGTIME); 
    dataTagShortTermLog.setTagValue(TAGVALUE);
    dataTagShortTermLog.setTimezone(TimeZone.getDefault().getID());
    dataTagLogMapper.insertLog(dataTagShortTermLog);
  }
}
