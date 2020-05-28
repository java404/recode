package smartmon.core.store.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import smartmon.core.mapper.MetaFileMapper;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.core.store.SmartMonStoreService;
import smartmon.utilities.misc.SmartMonTime;

@Service
public class SmartMonStoreServiceImpl implements SmartMonStoreService {
  private static final String FILE_STATUS_INIT = "init";
  private static final String FILE_STATUS_COMPLETED = "completed";

  @Value("${smartmon.store.rootFolder:/opt/smartmon/data}")
  private String storeFolder;

  @Autowired
  private MetaFileMapper metaFileMapper;

  @Autowired
  private AsyncTaskExecutor asyncTaskExecutor;

  private void checkInputFile(SmartMonStoreFile storeFile) {
    // TODO check the files with the type (e.g. PDS installer; SmartStor installer)
    storeFile.setStatus(FILE_STATUS_COMPLETED);
    storeFile.setErrorMessage("");
    metaFileMapper.updateStatus(storeFile);
  }

  @Override
  public SmartMonStoreFile put(SmartMonStoreInputFile localFile) {
    final SmartMonStoreFile storeFile = new SmartMonStoreFile();
    storeFile.setLocalFilename(localFile.getLocalFilename());
    storeFile.setLocalFileSize(localFile.getLocalFileSize());
    storeFile.setOriginalFilename(localFile.getOriginalFilename());
    storeFile.setTimestamp(SmartMonTime.now());
    storeFile.setType(localFile.getType());
    storeFile.setStatus(FILE_STATUS_INIT);
    metaFileMapper.put(storeFile);
    asyncTaskExecutor.submit(() -> checkInputFile(storeFile));
    return storeFile;
  }

  @Override
  public List<SmartMonStoreFile> findAll() {
    return metaFileMapper.findAll();
  }

  @Override
  public SmartMonStoreFile findById(long fileId) {
    return metaFileMapper.findById(fileId);
  }
}
