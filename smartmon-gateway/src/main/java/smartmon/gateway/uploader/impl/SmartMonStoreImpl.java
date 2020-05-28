package smartmon.gateway.uploader.impl;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.gateway.uploader.SmartMonCoreStore;
import smartmon.gateway.uploader.SmartMonStore;
import smartmon.gateway.uploader.exception.SmartMonUploaderError;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.FileUtilities;

@Slf4j
@Service
public class SmartMonStoreImpl implements SmartMonStore {
  @Value("${smartmon.store.rootFolder:/opt/smartmon/data}")
  private String storeFolder;

  @Autowired
  private SmartMonCoreStore smartMonCoreStore;

  private File makeStoreFile() throws IOException {
    return FileUtilities.createTempFile(storeFolder, "smartmon-", "-data");
  }

  @Override
  public SmartMonResponse<SmartMonStoreFile> put(String type, FilePart file) {
    final String originalFilename = file.filename();
    log.info("Saving file {}", originalFilename);

    try {
      final File storeFile = makeStoreFile();
      file.transferTo(storeFile);
      final SmartMonStoreInputFile inputFile = new SmartMonStoreInputFile();
      inputFile.setType(type);
      inputFile.setOriginalFilename(originalFilename);
      inputFile.setLocalFilename(storeFile.getName());
      inputFile.setLocalFileSize(storeFile.length());
      return smartMonCoreStore.put(inputFile);
    } catch (Exception error) {
      log.error("cannot save upload file: ", error);
      throw new SmartMonUploaderError();
    }
  }
}
