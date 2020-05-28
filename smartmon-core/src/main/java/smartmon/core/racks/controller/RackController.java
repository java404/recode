package smartmon.core.racks.controller;

import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.mapper.RackMapper;
import smartmon.core.racks.RackService;
import smartmon.core.racks.model.RackAllocation;
import smartmon.core.racks.vo.IdcRackAllocateVo;
import smartmon.core.racks.vo.RackAllocateVo;
import smartmon.core.racks.vo.RackAllocationVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;

@Api(tags = "racks")
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/racks")
@RestController
public class RackController {
  @Autowired
  private RackService rackService;
  @Autowired
  private RackMapper rackMapper;

  @ApiOperation("Get all rack allocation info")
  @GetMapping("allocated")
  public SmartMonResponse<List<RackAllocationVo>> getAll() {
    List<RackAllocation> racks = rackMapper.findAll();
    if (CollectionUtils.isEmpty(racks)) {
      return new SmartMonResponse<>(Lists.newArrayList());
    }
    List<RackAllocationVo> vos = BeanConverter.copy(racks, RackAllocationVo.class);
    return new SmartMonResponse<>(vos);
  }

  @ApiOperation("Add host to given rack position")
  @PostMapping("{rackName}/{rackIndex}/allocate")
  public SmartMonResponse<String> addHostToGivenRackPosition(
    @RequestParam(value = "idcId") String idcId,
    @PathVariable("rackName") String rackName,
    @PathVariable("rackIndex") Integer rackIndex,
    @RequestBody RackAllocateVo vo) {
    rackService.addHostToGivenRackPosition(idcId, rackName, rackIndex, vo.getHostUuid(), vo.getSize());
    return SmartMonResponse.OK;
  }

  @ApiOperation("Move host to given rack position")
  @PatchMapping("{rackName}/{rackIndex}/reallocate")
  public SmartMonResponse<String> moveHostToGivenRackPosition(
    @RequestParam(value = "idcId") String idcId,
    @PathVariable("rackName") String rackName,
    @PathVariable("rackIndex") Integer rackIndex,
    @RequestBody RackAllocateVo vo) {
    rackService.moveHostToGivenRackPosition(idcId, rackName, rackIndex, vo.getHostUuid(), vo.getSize());
    return SmartMonResponse.OK;
  }

  @ApiOperation("Remove host from given rack position")
  @DeleteMapping("{rackName}/{rackIndex}/unallocate")
  public SmartMonResponse<String> removeHostFromRackPosition(
    @RequestParam(value = "idcId") String idcId,
    @PathVariable("rackName") String rackName,
    @PathVariable("rackIndex") Integer rackIndex) {
    rackService.removeHostFromRackPosition(idcId, rackName, rackIndex);
    return SmartMonResponse.OK;
  }

  @ApiOperation("Add host to available rack")
  @PostMapping("allocate")
  public SmartMonResponse<String> addHostToAvailableRack(@RequestBody IdcRackAllocateVo vo) {
    rackService.addHostToAvailableRack(vo.getHostUuid(), vo.getSize(), vo.getIdcName());
    return SmartMonResponse.OK;
  }

  @ApiOperation("Rename rack")
  @PatchMapping("{rackName}/rename")
  public SmartMonResponse<String> rackRename(
    @RequestParam(value = "idcId") String idcId,
    @PathVariable String rackName, @RequestParam("newRackName") String newRackName) {
    rackService.renameRack(idcId, rackName, newRackName);
    return SmartMonResponse.OK;
  }
}
