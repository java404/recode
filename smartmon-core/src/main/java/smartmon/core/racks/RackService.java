package smartmon.core.racks;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartmon.core.mapper.IdcMapper;
import smartmon.core.mapper.RackMapper;
import smartmon.core.racks.exception.HostAlreadyInRackException;
import smartmon.core.racks.exception.IdcNotExistsException;
import smartmon.core.racks.exception.IdcRenameNotAllowedException;
import smartmon.core.racks.exception.RackPositionUnavailableException;
import smartmon.core.racks.exception.RackRenameNotAllowedException;
import smartmon.core.racks.model.Idc;
import smartmon.core.racks.model.Rack;
import smartmon.core.racks.model.RackAllocation;
import smartmon.core.racks.model.RackPosition;
import smartmon.core.racks.vo.IdcRackAllocateVo;

@Service
public class RackService {
  @Autowired
  private IdcMapper idcMapper;
  @Autowired
  private RackMapper rackMapper;

  @Transactional(rollbackFor = Exception.class)
  public void moveHostToGivenRackPosition(String idcId, String rackName, Integer rackIndex,
                                          String hostUuid, Integer size) {
    rackMapper.deleteByHostUuid(hostUuid);
    addHostToGivenRackPosition(idcId, rackName, rackIndex, hostUuid, size);
  }

  public void addHostToGivenRackPosition(String idcId, String rackName, Integer rackIndex,
                                         String hostUuid, Integer size) {
    checkIdcExists(idcId);
    checkHostInRack(hostUuid);
    List<RackAllocation> rackAllocationList = rackMapper.findByIdcIdAndRackName(idcId, rackName);
    Rack rack = Rack.create(idcId, rackName, rackAllocationList);
    if (!rack.positionAvailable(rackIndex, size)) {
      throw new RackPositionUnavailableException(
        String.format("Rack:[%s] postion:[%s] is unavailable", rackName, rackIndex));
    }
    RackAllocation rackAllocation = new RackAllocation(idcId, rackName, rackIndex, hostUuid, size);
    rackMapper.save(rackAllocation);
  }

  private void checkIdcExists(String idcId) {
    Idc idc = idcMapper.findById(idcId);
    if (idc == null) {
      throw new IdcNotExistsException(String.format("Idc:[%s] not exists", idcId));
    }
  }

  public void removeHostFromRackPosition(String idcId, String rackName, Integer rackIndex) {
    rackMapper.delete(idcId, rackName, rackIndex);
  }

  @Transactional(rollbackFor = Exception.class)
  public void addHostToAvailableRackBatch(List<IdcRackAllocateVo> vos) {
    for (IdcRackAllocateVo vo : vos) {
      addHostToAvailableRack(vo.getHostUuid(), vo.getSize(), vo.getIdcName());
    }
  }

  public void addHostToAvailableRack(String hostUuid, Integer size) {
    addHostToAvailableRack(hostUuid, size, null);
  }

  private void addHostToAvailableRack(String hostUuid, Integer size, String idcName) {
    checkHostInRack(hostUuid);
    RackPosition rackPosition = null;
    if (StringUtils.isNotEmpty(idcName)) {
      Idc idc = addIdcIfAbsent(idcName);
      rackPosition = getRackPosition(size, idc);
    } else {
      rackPosition = getRackPosition(size, null);
    }
    RackAllocation rackAllocation = new RackAllocation(rackPosition.getIdcId(), rackPosition.getRackName(),
      rackPosition.getRackIndex(), hostUuid, size);
    rackMapper.save(rackAllocation);
  }

  private void checkHostInRack(String hostUuid) {
    List<RackAllocation> rackAllocationsOfHost = rackMapper.getByHostUuid(hostUuid);
    if (CollectionUtils.isNotEmpty(rackAllocationsOfHost)) {
      String rack = rackAllocationsOfHost.get(0).getRackName();
      throw new HostAlreadyInRackException(String.format("Host:[%s] already in rack:[%s]", hostUuid, rack));
    }
  }

  private RackPosition getRackPosition(Integer size, Idc idc) {
    List<String> idcIds;
    if (idc == null) {
      idcIds = idcMapper.getIdcIds();
    } else {
      idcIds = Collections.singletonList(idc.getId());
    }
    for (String idcId : idcIds) {
      List<String> rackNames = rackMapper.getRackNamesByIdcId(idcId);
      for (String rackName : rackNames) {
        List<RackAllocation> rackAllocations = rackMapper.findByIdcIdAndRackName(idcId, rackName);
        Rack rack = Rack.create(idcId, rackName, rackAllocations);
        Integer availablePosition = rack.getAvailablePosition(size);
        if (availablePosition != null) {
          return RackPosition.create(idcId, rackName, availablePosition);
        }
      }
    }
    if (idcIds.size() > 0) {
      return RackPosition.create(idcIds.get(0));
    } else {
      Idc idcNew = addIdcIfAbsent(Constants.DEFAULT_IDC);
      return RackPosition.create(idcNew.getId());
    }
  }

  public void renameRack(String idcId, String oldRackName, String newRackName) {
    if (oldRackName.equals(newRackName)) {
      throw new RackRenameNotAllowedException(
        String.format("Rack:[%s] name is not changed", oldRackName));
    }
    int count = rackMapper.getRecordCountByIdcAndRackName(idcId, newRackName);
    if (count > 0) {
      throw new RackRenameNotAllowedException(
        String.format("Rack:[%s] is already exists", newRackName));
    }
    rackMapper.renameRack(idcId, oldRackName, newRackName);
  }

  public List<Idc> addIdcs(List<String> idcNames) {
    List<Idc> idcs = Lists.newArrayList();
    for (String idcName : idcNames) {
      Idc idc = addIdcIfAbsent(idcName);
      idcs.add(idc);
    }
    return idcs;
  }

  public Idc addIdcIfAbsent(String idcName) {
    Idc idc = idcMapper.findByName(idcName);
    if (idc == null) {
      idc = new Idc(UUID.randomUUID().toString(), idcName);
      idcMapper.save(idc);
    }
    return idc;
  }

  public void renameIdc(String idcName, String newIdcName) {
    if (idcName.equals(newIdcName)) {
      throw new IdcRenameNotAllowedException(String.format("IDC:[%s] name is not changed", idcName));
    }
    int count = idcMapper.getCountByName(newIdcName);
    if (count > 0) {
      throw new IdcRenameNotAllowedException(String.format("IDC:[%s] is already exists", newIdcName));
    }
    idcMapper.renameIdc(idcName, newIdcName);
  }
}
