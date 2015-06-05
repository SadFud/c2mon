/**
 * Copyright (c) 2015 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.c2mon.daq.spectrum.listener.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.c2mon.daq.spectrum.SpectrumEvent;
import cern.c2mon.daq.spectrum.SpectrumEventProcessor;
import cern.c2mon.daq.spectrum.listener.SpectrumListenerIntf;

/**
 * The message reception thread. The thread is waiting on a given TCP port, for each incoming request
 * a single line is taken from the socket and converted into an Event object placed on the event queue.
 * 
 * To smoothly stop the thread, call shutdown().
 * 
 * @author mbuttner
 * @version 29 May 2009
 * 
 */
public class SpectrumListenerTcp implements Runnable, SpectrumListenerIntf {

    private static final Logger LOG = LoggerFactory.getLogger(SpectrumListenerTcp.class);
    private static SpectrumListenerTcp listener;
    
    private boolean cont = true;
    private SpectrumEventProcessor proc;
    private Queue<SpectrumEvent> eventQueue;
    
    //
    // --- CONSTRUCTION --------------------------------------------------------------------
    //
    private SpectrumListenerTcp() {
        
    }

    public static SpectrumListenerTcp getInstance() {
        if (listener == null) {
            listener = new SpectrumListenerTcp();
        }
        return listener;
    }
    //
    // --- PUBLIC METHODS -------------------------------------------------------------------
    //
    
    /**
     * Sets the "cont" flag to false, so that the thread stops after the next request.
     */
    @Override
    public void shutdown() {
        cont = false;
    }
        
    /**
     * The thread's main. Listens on the Server.port and accepts incoming requests. Each message
     * is considered to be single line. The message is turned into an instance of Event and added
     * to the event queue for further processing.
     */
    @Override
    public void run() {
        try {
            int loopCounter = 0;
            ServerSocket srvr = new ServerSocket(proc.getPort());
            while (cont) {
                try {
                    loopCounter++;
                    Socket skt = srvr.accept();
                        
                    // Add the originating server 
                    InetAddress ia = skt.getInetAddress();
                    String serverName = ia.getHostName();
                        
                    if (serverName.equals(proc.getPrimaryServer()) || 
                        serverName.equals(proc.getSecondaryServer())) {                 
                        
                        LOG.info("Request " + loopCounter + " received from " + serverName);
                        BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                        while (!in.ready()) { /**/ }
                        String msg = in.readLine();
                        eventQueue.add(new SpectrumEvent(serverName, msg));
                        in.close();
                        LOG.info("Event " + msg + " processed.");
                    } else {
                        LOG.error("Rejected request from " + serverName + " (this server IS NOT on the whitelist !!!)");
                    }
                    skt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            srvr.close();
        } catch(Exception eFatal) {
            eFatal.printStackTrace();
        }
    }

    @Override
    public void setProcessor(SpectrumEventProcessor proc) {
        this.proc = proc;
        this.eventQueue = proc.getQueue();
        LOG.info("Listener enabled.");
    }
    
}

    
