package smartmon.vhe.deployment.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.core.provider.SmartMonCoreProvider;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.utilities.general.SmartMonException;
import smartmon.vhe.config.SmartMonVheErrno;

@Slf4j
@Service
public class SmartstorTemplateService {
  @Value("${smartmon.store.rootFolder:/opt/smartmon/data}")
  private String storeFolder;
  @DubboReference(version = "${dubbo.service.version}", check = false, lazy = true)
  private SmartMonCoreProvider smartMonCoreProvider;

  public String getTemplate(Long fileId) {
    String installerFilepath = getFileAbsolutePath(fileId);
    String templateFilepath = getTemplateFilepath();
    generateTemplateFile(installerFilepath, templateFilepath);
    return readTemplateFromFile(templateFilepath);
  }

  private String getFileAbsolutePath(Long fileId) {
    SmartMonStoreFile smartMonStoreFile = smartMonCoreProvider.findById(fileId);
    if (smartMonStoreFile == null) {
      throw new SmartMonException(SmartMonVheErrno.SMARTSTOR_INSTALLER_NOT_FOUND, String.valueOf(fileId));
    }
    String filename = smartMonStoreFile.getLocalFilename();
    String filepath = storeFolder.endsWith("/") ? storeFolder : storeFolder + "/";
    File file = new File(filepath + filename);
    if (!file.exists()) {
      throw new SmartMonException(SmartMonVheErrno.SMARTSTOR_INSTALLER_NOT_FOUND, String.valueOf(fileId));
    }
    return file.getAbsolutePath();
  }

  private String getTemplateFilepath() {
    int randomValue = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    return String.format("/tmp/args-%s.json", randomValue);
  }

  private void generateTemplateFile(String installerFilepath, String templateFilepath) {
    String generateFileCommand = String.format("chmod +x %s && timeout 30 %s --gen-args-file=%s",
      installerFilepath, installerFilepath, templateFilepath);
    try {
      String[] commands = {"/bin/sh", "-c", generateFileCommand};
      Process process = Runtime.getRuntime().exec(commands);
      process.waitFor();
      process.destroy();
    } catch (IOException | InterruptedException err) {
      String message = "Generate smartstor args template failed";
      log.error(message, err);
      throw new SmartMonException(SmartMonVheErrno.GET_SMARTSTOR_ARGS_TEMPLATE_ERROR, message);
    }
  }

  private String readTemplateFromFile(String templateFilepath) {
    File templateFile = Paths.get(templateFilepath).toFile();
    if (!templateFile.exists()) {
      String message = "Smartstor args template is not exists";
      throw new SmartMonException(SmartMonVheErrno.GET_SMARTSTOR_ARGS_TEMPLATE_ERROR, message);
    }
    try {
      String templateContent = Files.lines(templateFile.toPath()).collect(Collectors.joining());
      Files.delete(templateFile.toPath());
      return templateContent;
    } catch (IOException err) {
      String message = "Read smartstor args template failed";
      log.error(message, err);
      throw new SmartMonException(SmartMonVheErrno.GET_SMARTSTOR_ARGS_TEMPLATE_ERROR, message);
    }
  }
}
