/******************************************************************************
 * Copyright (C) 2010-2016 CERN. All rights not expressly granted are reserved.
 * <p>
 * This file is part of the CERN Control and Monitoring Platform 'C2MON'.
 * C2MON is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the license.
 * <p>
 * C2MON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with C2MON. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package cern.c2mon.server.cache.dbaccess;

import cern.c2mon.server.test.DatabasePopulationRule;
import cern.c2mon.server.cache.dbaccess.structure.DBBatch;
import cern.c2mon.server.common.alarm.AlarmCacheObject;
import cern.c2mon.server.common.alarm.AlarmCondition;
import cern.c2mon.server.test.CacheObjectComparison;
import cern.c2mon.server.test.CacheObjectCreation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the iBatis AlarmMapper.
 *
 * @author Mark Brightwell
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:config/server-cachedbaccess.xml"
})
@TestPropertySource("classpath:c2mon-server-default.properties")
//@TransactionConfiguration(transactionManager = "cacheTransactionManager", defaultRollback = true)
//@Transactional
//@ContextConfiguration({"classpath:config/server-cachedbaccess-test.xml"})
public class AlarmMapperTest {

  @Rule
  @Autowired
  public DatabasePopulationRule databasePopulationRule;

  @Resource
  private AlarmMapper alarmMapper;

//  /**
//   * Tests the alarm is inserted and retrieved correctly.
//   */
//  @Test
//  public void testInsertAndRetrieve() {
//    AlarmCacheObject alarmOriginal = CacheObjectCreation.createTestAlarm3();
//    AlarmCacheObject alarmRetrieved = (AlarmCacheObject) alarmMapper.getItem(alarmOriginal.getId());
//    CacheObjectComparison.equals(alarmOriginal, alarmRetrieved);
//  }

  /**
   * Compares size of table with number of records.
   */
  @Test
  public void testGetAll() {
    int tableSize = alarmMapper.getNumberItems();
    int alarmsRetrieved = alarmMapper.getAll().size();
    assertEquals(tableSize, alarmsRetrieved);
  }

  /**
   * Checks runs.
   */
  @Test
  public void testGetMaxId() {
    alarmMapper.getNumberItems();
  }

  /**
   * Retrieves a batch of 10 and checks the number
   * retrieved is correct (checks > 10 in table).
   */
  @Test
  public void testGetRowBatch() {
    int tableSize = alarmMapper.getNumberItems();
    int alarmsRetrieved = alarmMapper.getRowBatch(new DBBatch(1L, 400000L)).size();
    if (tableSize >= 10) {
      assertEquals(10, alarmsRetrieved);
    } else {
      assertEquals(tableSize, alarmsRetrieved);
    }
  }

  @Test
  public void testUpdateAlarm() {
    AlarmCacheObject alarmOriginal = (AlarmCacheObject) alarmMapper.getItem(350000L);
    //check is terminated
    assertEquals(alarmOriginal.getState(), AlarmCondition.TERMINATE);
    //update fields
    alarmOriginal.setState(AlarmCondition.ACTIVE);
    alarmOriginal.setTimestamp(new Timestamp(System.currentTimeMillis()));
    alarmOriginal.setInfo("updated info");
    alarmOriginal.hasBeenPublished(new Timestamp(System.currentTimeMillis() - 100));
    assertTrue(alarmOriginal.isPublishedToLaser());
    assertTrue(alarmOriginal.getLastPublication() != null);
    //update in DB
    alarmMapper.updateCacheable(alarmOriginal);
    //retrieve from DB
    AlarmCacheObject alarmRetrieved = (AlarmCacheObject) alarmMapper.getItem(alarmOriginal.getId());
    //compare
    CacheObjectComparison.equals(alarmOriginal, alarmRetrieved);
  }

  @Test
  public void testIsInDB() {
    assertTrue(alarmMapper.isInDb(350000L));
  }

  @Test
  public void testNotInDB() {
    assertFalse(alarmMapper.isInDb(450000L));
  }

}

