/******************************************************************************
 * Copyright (C) 2010-2016 CERN. All rights not expressly granted are reserved.
 * 
 * This file is part of the CERN Control and Monitoring Platform 'C2MON'.
 * C2MON is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the license.
 * 
 * C2MON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with C2MON. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package cern.c2mon.client.core;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import cern.c2mon.client.common.listener.BaseTagListener;
import cern.c2mon.client.common.tag.Tag;

/**
 * Integration test of Client API modules.
 * 
 * @author Mark Brightwell
 * 
 */
public class C2monServiceGatewayTest {

  /**
   * Log4j instance
   */
  private static final Logger LOG = LoggerFactory.getLogger(C2monServiceGatewayTest.class);

  @Test
  public void startClient() throws InterruptedException, MBeanRegistrationException, InstanceNotFoundException, MalformedObjectNameException,
      NullPointerException {
    C2monServiceGateway.startC2monClient();
    assertNotNull(C2monServiceGateway.getCommandManager());
    assertNotNull(C2monServiceGateway.getCommandService());
    assertNotNull(C2monServiceGateway.getSupervisionService());
    assertNotNull(C2monServiceGateway.getSupervisionManager());
    assertNotNull(C2monServiceGateway.getTagService());
    assertNotNull(C2monServiceGateway.getAlarmService());
    assertNotNull(C2monServiceGateway.getConfigurationService());
//    assertNotNull(C2monServiceGateway.getSessionService());
    assertNotNull(C2monServiceGateway.getStatisticsService());
    assertNotNull(C2monServiceGateway.getTagManager());
  }

  /**
   * Needs running without JVM properties set in order to test correct loading
   * into Spring context.
   * 
   * @throws InterruptedException
   */
  // @Test
  public void startClientWithProperties() throws InterruptedException {
    System.setProperty("c2mon.client.conf.url", "classpath:test-config/test-properties.txt");
    C2monServiceGateway.startC2monClient();
    assertNotNull(C2monServiceGateway.getCommandService());
    assertNotNull(C2monServiceGateway.getSupervisionService());
    assertNotNull(C2monServiceGateway.getTagService());
  }

  /**
   * Starts the C2MON client API and registers to some tags
   * 
   * @param args
   */
  public static void main(String[] args) {
    // Put here your list of tags that you want to test!
    Set<Long> tagIds = new HashSet<Long>();
    tagIds.add(159195L);
    tagIds.add(159135L);
    tagIds.add(156974L);
    tagIds.add(187252L);
    tagIds.add(187248L);
    tagIds.add(187208L);
    tagIds.add(187244L);
    tagIds.add(187227L);
    tagIds.add(165354L);
    tagIds.add(159207L);

    try {
      C2monServiceGateway.startC2monClient();
      try {
        // Sleep to give the JmsProxy time to connect (Should be removed soon!)
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      TagService tagManager = C2monServiceGateway.getTagService();
      tagManager.subscribe(tagIds, new BaseTagListener() {
        @Override
        public void onUpdate(Tag tagUpdate) {
          System.out.println("Update received for tag " + tagUpdate.getId() + ":");
          System.out.println("\ttag name           : " + tagUpdate.getName());
          System.out.println("\tvalue              : " + tagUpdate.getValue());
          System.out.println("\ttype               : " + tagUpdate.getTypeNumeric());
          System.out.println("\tvalue description  : " + tagUpdate.getDescription());
          System.out.println("\tquality Code       : " + tagUpdate.getDataTagQuality().toString());
          System.out.println("\tquality description: " + tagUpdate.getDataTagQuality().getDescription());
          System.out.println();
        }
      });
    } catch (Exception e) {
      LOG.error("Catched runtime exception on main thread.", e);
      System.exit(1);
    }
  }
}