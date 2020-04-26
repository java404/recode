package smartmon.core.racks.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.racks.IdcMapper;
import smartmon.core.racks.RackService;
import smartmon.core.racks.model.Idc;
import smartmon.core.racks.vo.IdcAddVo;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "idcs")
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/idcs")
@RestController
public class IdcController {
  @Autowired
  private IdcMapper idcMapper;
  @Autowired
  private RackService rackService;

  @ApiOperation("Get all idc info")
  @GetMapping
  public SmartMonResponse<List<Idc>> getAll() {
    return new SmartMonResponse<>(idcMapper.findAll());
  }

  @ApiOperation("Add idc")
  @PostMapping
  public SmartMonResponse add(@RequestBody IdcAddVo vo) {
    Idc idc = rackService.addIdcIfAbsent(vo.getName());
    return new SmartMonResponse<>(idc);
  }

  @ApiOperation("Rename")
  @PatchMapping("{name}/rename")
  public SmartMonResponse rename(@PathVariable String name, @RequestParam("newName") String newName) {
    rackService.renameIdc(name, newName);
    return SmartMonResponse.OK;
  }
}
