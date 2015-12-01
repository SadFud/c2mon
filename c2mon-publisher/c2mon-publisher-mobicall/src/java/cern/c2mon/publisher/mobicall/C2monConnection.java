/**
 * Copyright (c) 2015 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.c2mon.publisher.mobicall;

import java.util.Collection;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.c2mon.client.common.tag.Tag;
import cern.c2mon.client.core.C2monServiceGateway;
import cern.c2mon.client.jms.AlarmListener;
import cern.c2mon.shared.client.alarm.AlarmValue;

/**
 * Implementation of C2monConnectionIntf for the real C2MON connection. This is the class
 * to be used in the Spring configuration for production purpose.
 * 
 * @author mbuttner
 */
public class C2monConnection implements C2monConnectionIntf {

    private static final Logger LOG = LoggerFactory.getLogger(C2monConnection.class);
    private AlarmListener listener;
    private volatile boolean cont;
    
    //
    // --- Implements C2monConnectionInterface ----------------------------------
    //
    @Override
    public void setListener(AlarmListener listener) {
        this.listener = listener;                       
    }
    
    @Override
    public void start() throws Exception {
        cont = true;
        C2monServiceGateway.startC2monClient();
        C2monConnectionMonitor.start();
        while (!C2monServiceGateway.getSupervisionService().isServerConnectionWorking() && cont) {
            LOG.info("Awaiting connection ...");
            Thread.sleep(1000);
        }
    }
    
    @Override
    public void connectListener() throws JMSException {
        LOG.info("Connecting alarm listener ...");
        C2monServiceGateway.getAlarmService().addAlarmListener(listener);        
    }
    
    @Override
    public void stop() {
        cont = false;
        LOG.debug("Stopping the C2MON client...");
        try {
            C2monServiceGateway.getAlarmService().removeAlarmListener(listener);
        } catch (JMSException e) {
            LOG.warn("?", e);
        }
        LOG.info("C2MON client stopped.");

    }

    @Override
    public Collection<AlarmValue> getActiveAlarms() {
        return C2monServiceGateway.getAlarmService().getAllActiveAlarms();
    }

    @Override
    public int getQuality(long alarmTagId) {
        int qual = 0;
        Tag tag = C2monServiceGateway.getTagService().get(alarmTagId);
        if (tag != null) {
            if (tag.getDataTagQuality().isValid()) {
                qual = qual | Quality.VALID;
            }
            if (tag.getDataTagQuality().isExistingTag()) {
                qual = qual | Quality.EXISTING;
            }
        }
        return qual;
    }
}