package smartmon.vhe.service;

import java.util.List;

import smartmon.taskmanager.types.TaskGroup;
import smartmon.vhe.service.dto.HostInitDto;
import smartmon.vhe.service.dto.VheStorageHostDto;

public interface HostService {
  TaskGroup init(List<HostInitDto> hostParameters);

  void registHost(HostInitDto hostInitDto);

  void installAgent();

  List<VheStorageHostDto> listAll();
}
