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
package cern.c2mon.client.core.tag;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cern.c2mon.client.common.tag.CommandTag;
import cern.c2mon.client.core.C2monServiceGateway;
import cern.c2mon.shared.client.command.CommandTagHandleImpl;
import cern.c2mon.shared.client.command.CommandTagHandleImpl.Builder;
import cern.c2mon.shared.client.command.CommandTagValueException;
import cern.c2mon.shared.client.command.RbacAuthorizationDetails;
import cern.c2mon.shared.common.datatag.address.HardwareAddress;
import cern.c2mon.shared.common.datatag.address.impl.SimpleHardwareAddressImpl;

public class CommandTagImplTest {

  @Test
  public void testXMLSerialization() throws Exception {
      
    CommandTagImpl<String> commandTag = createCommandTag(12342L);
    
    String xml = commandTag.getXml();
    commandTag.toString();
    
    CommandTagImpl<String> newCommandTag = CommandTagImpl.fromXml(xml);
    
    assertTrue(commandTag.getClientTimeout() == newCommandTag.getClientTimeout());
    assertTrue(commandTag.getId().equals(newCommandTag.getId()));
    assertTrue(commandTag.getMaxValue().equals(newCommandTag.getMaxValue()));
    assertTrue(commandTag.getHardwareAddress().equals(newCommandTag.getHardwareAddress()));
  }

  /**
   * This test is useful when checking that changes made on the server are supported by the client. However
   * it is not a good idea to run this in a continuous deployment environment since the test is
   * dependent on the life test system.
   */
//  @Test
  public void startClientWithProperties() throws InterruptedException {
    System.setProperty("c2mon.client.conf.url", "http://timweb/test/conf/c2mon-client.properties");
    C2monServiceGateway.startC2monClientSynchronous();
    CommandTag<Boolean> commandTag = C2monServiceGateway.getCommandService().getCommandTag(104974L);
    assertNotNull(commandTag.getName());
    assertTrue(!commandTag.getName().equalsIgnoreCase(""));
    assertNotNull(commandTag.getProcessId());
    assertNotNull(commandTag.getHardwareAddress());
  }
  
  /**
   * Private helper method. Creates CommandTagImpl.
   */  
  private CommandTagImpl createCommandTag (Long id) {
    
    CommandTagImpl commandTag = new CommandTagImpl(id);
    commandTag.update(createCommandTagHandleImpl(id));
    
    try {
      commandTag.setValue((Object)(new String("HI!")));
    } catch (CommandTagValueException e) {
      e.printStackTrace();
    }
    
    return commandTag;
  }
  
  /**
   * Private helper method. Used to create CommandTagImpl.
   */
  private CommandTagHandleImpl<String> createCommandTagHandleImpl (final Long id) {
    Builder<String> builder = new Builder<String>(id);
    builder = builder.clientTimeout(666)
                     .dataType(String.class.getName())
                     .description("test descriptor")
                     .hardwareAddress(createHardwareAddress())
                     .maxValue("666")
                     .minValue("1")
                     .name("test name")
                     .rbacAuthorizationDetails(createAuthDetails())
                     .processId(123L);
    
    return new CommandTagHandleImpl<String>(builder);
  }
  
  /**
   * Private helper method. Used to create CommandTagImpl.
   */
  private RbacAuthorizationDetails createAuthDetails() {

    RbacAuthorizationDetails authDetails = new RbacAuthorizationDetails();
    
    authDetails.setRbacClass("Manos");
    authDetails.setRbacDevice("Mark");
    authDetails.setRbacProperty("Matias");
    
    return authDetails;
  }
  
  private HardwareAddress createHardwareAddress() {
   return new SimpleHardwareAddressImpl("test-address");
  }
}
