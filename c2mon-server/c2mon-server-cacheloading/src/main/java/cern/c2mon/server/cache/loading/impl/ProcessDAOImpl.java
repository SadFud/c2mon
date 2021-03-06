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
package cern.c2mon.server.cache.loading.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cern.c2mon.server.cache.dbaccess.ProcessMapper;
import cern.c2mon.server.cache.loading.ProcessDAO;
import cern.c2mon.server.cache.loading.common.AbstractDefaultLoaderDAO;
import cern.c2mon.server.common.process.Process;

/**
 * Process DAO implementation.
 *
 * @author Mark Brightwell
 *
 */
@Service("processDAO")
public class ProcessDAOImpl extends AbstractDefaultLoaderDAO<Process> implements ProcessDAO {

  private ProcessMapper processMapper;

  @Autowired
  public ProcessDAOImpl(ProcessMapper processMapper) {
    super(500, processMapper); // initial buffer size
    this.processMapper = processMapper;
  }

  @Override
  public void deleteProcess(Long processId) {
    processMapper.deleteProcess(processId);
  }

  @Override
  public void deleteItem(Long id) {
    processMapper.deleteProcess(id);
  }

  @Override
  public void insert(Process process) {
    processMapper.insertProcess(process);
  }

  @Override
  public void updateConfig(Process process) {
    processMapper.updateProcessConfig(process);
  }

  @Override
  protected Process doPostDbLoading(Process process) {
    return process;
  }

  @Override
  public Integer getNumTags(Long processId) {
    return processMapper.getNumTags(processId);
  }

  @Override
  public Integer getNumInvalidTags(Long processId) {
    return processMapper.getNumInvalidTags(processId);
  }

  @Override
  public Long getIdByName(String name) {
    return processMapper.getIdByName(name);
  }
}
