package smartmon.gateway.uploader.impl;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import smartmon.core.provider.SmartMonCoreProvider;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.gateway.uploader.SmartMonStore;
import smartmon.gateway.uploader.exception.SmartMonUploaderError;
import smartmon.utilities.misc.FileUtilities;

@Slf4j
@Service
public class SmartMonStoreImpl implements SmartMonStore {
  @Value("${smartmon.store.rootFolder:/opt/smartmon/data}")
  private String storeFolder;

  @DubboReference(version = "${dubbo.service.version}", check = false, lazy = true)
  private SmartMonCoreProvider smartMonCoreProvider;

  private File makeStoreFile() throws IOException {
    return FileUtilities.createTempFile(storeFolder, "smartmon-", "-data");
  }

  @Override
  public SmartMonStoreFile put(String type, String desc, FilePart file) {
    final String originalFilename = file.filename();
    log.info("Saving file {}", originalFilename);

    try {
      final File storeFile = makeStoreFile();
      file.transferTo(storeFile);
      final SmartMonStoreInputFile inputFile = new SmartMonStoreInputFile();
      inputFile.setType(type);
      inputFile.setDesc(Strings.nullToEmpty(desc));
      inputFile.setOriginalFilename(originalFilename);
      inputFile.setLocalFilename(storeFile.getName());
      inputFile.setLocalFileSize(storeFile.length());
      return smartMonCoreProvider.put(inputFile);
    } catch (Exception error) {
      log.error("cannot save upload file: ", error);
      throw new SmartMonUploaderError();
    }
  }
}
