package smartmon.vhe.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.idc.vo.IdcVo;
import smartmon.core.racks.vo.RackAllocationVo;
import smartmon.smartstor.web.dto.StorageHostDto;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.misc.TaskSynchronizer;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.taskmanager.vo.TaskStepVo;
import smartmon.utilities.misc.BeanConverter;
import smartmon.vhe.exception.HostInitException;
import smartmon.vhe.service.HostService;
import smartmon.vhe.service.dto.HostAddToRackDto;
import smartmon.vhe.service.dto.HostInitDto;
import smartmon.vhe.service.dto.HostRegistrationDto;
import smartmon.vhe.service.dto.StorageHostInitDto;
import smartmon.vhe.service.dto.VheStorageHostDto;
import smartmon.vhe.service.feign.SmartStorFeignClient;
import smartmon.vhe.service.feign.SmartmonCoreFeignClient;

@Service
@Slf4j
public class HostServiceImpl implements HostService {
  @Autowired
  private SmartmonCoreFeignClient coreFeignClient;
  @Autowired
  private SmartStorFeignClient smartStorFeignClient;
  @Autowired
  private TaskManagerService taskManagerService;

  @Override
  public TaskGroup init(List<HostInitDto> hostInitDtos) {
    List<TaskDescription> tasks = hostInitDtos.stream().map(this::taskDescription).collect(Collectors.toList());
    TaskGroup taskGroup = taskManagerService.createTaskGroup("InitHost", tasks);
    taskManagerService.invokeTaskGroup(taskGroup);
    return taskGroup;
  }

  private TaskDescription taskDescription(HostInitDto hostInitDto) {
    return new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_INIT).withResource(TaskRes.RES_NODE).withParameters(hostInitDto)
      .withStep("REGIST", "regist host", () -> registHost(hostInitDto))
      .withStep("INSTALL", "install agent", () -> installAgent(retrieveHostUuidFromTaskContext()))
      .build();
  }

  private void registHost(HostInitDto hostInitDto) {
    appendLogToTaskContext("regist host");
    SmartMonHost smartMonHost = registHost(hostInitDto.getHostRegistrationDto());
    appendLogToTaskContext(String.format("regist host success[%s]", smartMonHost.getHostUuid()));
    saveHostUuidToTaskContext(smartMonHost.getHostUuid());
    appendLogToTaskContext("init storage host");
    initStorageHost(getStorageHostInitDto(hostInitDto));
    appendLogToTaskContext("init storage host success");
    appendLogToTaskContext("add host to rack");
    addHostToRack(getHostAddToRackDto(hostInitDto));
    appendLogToTaskContext("add host to rack success");
  }

  private SmartMonHost registHost(HostRegistrationDto dto) {
    try {
      return coreFeignClient.addHost(dto).getContent();
    } catch (Exception e) {
      log.error("host registration failed", e);
      throw new HostInitException(e.getMessage());
    }
  }

  private void initStorageHost(StorageHostInitDto dto) {
    try {
      smartStorFeignClient.initHost(dto);
    } catch (Exception e) {
      log.error("storage host registration failed", e);
      throw new HostInitException(e.getMessage());
    }
  }

  private void addHostToRack(HostAddToRackDto dto) {
    try {
      coreFeignClient.addHostToRack(dto);
    } catch (Exception e) {
      log.error("host add to rack failed", e);
      throw new HostInitException(e.getMessage());
    }
  }

  private void installAgent(String hostUuid) {
    try {
      TaskGroupVo taskGroupVo = coreFeignClient.installAgent(hostUuid).getContent();
      String taskGroupId = taskGroupVo.getTaskGroupId().toString();
      Supplier<TaskGroupVo> taskResultSupplier = () -> coreFeignClient.getByTaskId(taskGroupId).getContent();
      Consumer<TaskGroupVo> logConsumer = taskGroup -> {
        String logs = taskGroup.getTasks().get(0).getSteps().stream()
          .map(TaskStepVo::getStepLog)
          .filter(StringUtils::isNotEmpty)
          .collect(Collectors.joining());
        TaskContext.currentTaskContext().getCurrentStep().updateLog(logs);
      };
      TaskSynchronizer.awaitCompletion(taskResultSupplier, logConsumer);
    } catch (Exception e) {
      log.error("install agent failed", e);
      throw new HostInitException(e.getMessage());
    }
  }

  private StorageHostInitDto getStorageHostInitDto(HostInitDto hostInitDto) {
    StorageHostInitDto dto = hostInitDto.getStorageHostRegistrationDto();
    dto.setGuid(retrieveHostUuidFromTaskContext());
    return dto;
  }

  private HostAddToRackDto getHostAddToRackDto(HostInitDto hostInitDto) {
    HostAddToRackDto dto = hostInitDto.getHostAddToRackDto();
    dto.setHostUuid(retrieveHostUuidFromTaskContext());
    return dto;
  }

  private void appendLogToTaskContext(String log) {
    TaskContext.currentTaskContext().getCurrentStep().appendLog(log);
  }

  private void saveHostUuidToTaskContext(String hostUuid) {
    TaskContext.currentTaskContext().getCurrentStep().setDetail(hostUuid);
  }

  private String retrieveHostUuidFromTaskContext() {
    Object result = TaskContext.currentTaskContext().getSteps().get(0).getDetail();
    return Objects.toString(result);
  }

  @Override
  public List<VheStorageHostDto> listAll() {
    List<VheStorageHostDto> hosts = new ArrayList<>();
    List<StorageHostDto> smartstorHosts = smartStorFeignClient.getStorageHosts().getContent();
    List<RackAllocationVo> racks = coreFeignClient.getRacks().getContent();
    Map<String, RackAllocationVo> hostIdRackMap = racks
      .stream()
      .collect(Collectors.toMap(RackAllocationVo::getHostUuid, Function.identity(), (oldValue, newValue) -> newValue));
    List<IdcVo> idcVos = coreFeignClient.getIdcs().getContent();
    Map<String, String> idcMap = idcVos
      .stream()
      .collect(Collectors.toMap(IdcVo::getId, IdcVo::getName, (oldValue, newValue) -> newValue));
    smartstorHosts.forEach(h -> {
      VheStorageHostDto storageHostDto = BeanConverter.copy(h, VheStorageHostDto.class);
      if (storageHostDto == null) {
        return;
      }
      RackAllocationVo rackAllocationVo = hostIdRackMap.get(storageHostDto.getGuid());
      if (rackAllocationVo != null) {
        storageHostDto.setRackInfo(rackAllocationVo);
      }
      String idcName = idcMap.get(storageHostDto.getIdcId());
      storageHostDto.setIdcName(idcName);
      hosts.add(storageHostDto);
    });
    return hosts;
  }
}
