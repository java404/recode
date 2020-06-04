package smartmon.injector.upload;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileUploadService {
  public void uploadFileByMappedByteBuffer(FileChunkParameter param, String targetFilepath) throws IOException {
    String fileName = param.getName();
    String tempFileName = fileName + "_tmp";
    String filepath = targetFilepath + param.getPath();
    File tmpFile = new File(filepath, tempFileName);
    if (!tmpFile.getParentFile().exists()) {
      if (!tmpFile.getParentFile().mkdirs()) {
        log.warn("make dir failed: {}", tmpFile.getParent());
      }
    }
    RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
    long offset = UploadConstants.CHUNK_SIZE * param.getChunk();
    byte[] fileData = param.getBytes();
    tempRaf.seek(offset);
    tempRaf.write(fileData);
    tempRaf.close();
    boolean isOk = checkAndSetUploadProgress(param, filepath);
    if (isOk) {
      boolean flag = renameFile(tmpFile, fileName);
      log.info("upload complete " + flag + " name=" + fileName);
      File file = new File(filepath, fileName);
      flag = file.setExecutable(true, false);
      if (!flag) {
        log.warn("set executable " + flag + " name=" + fileName);
      }
    }
  }

  private boolean checkAndSetUploadProgress(FileChunkParameter param, String uploadDirPath) throws IOException {
    String fileName = param.getName();
    File confFile = new File(uploadDirPath, fileName + ".conf");
    RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
    log.info("set part " + param.getChunk() + " complete");
    accessConfFile.setLength(param.getChunks());
    accessConfFile.seek(param.getChunk());
    accessConfFile.write(Byte.MAX_VALUE);
    byte[] completeList = FileUtils.readFileToByteArray(confFile);
    byte isComplete = Byte.MAX_VALUE;
    for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
      isComplete = (byte) (isComplete & completeList[i]);
      log.info("check part " + i + " complete?:" + completeList[i]);
    }
    accessConfFile.close();
    return isComplete == Byte.MAX_VALUE;
  }

  private boolean renameFile(File toBeRenamed, String newFilename) {
    if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
      return false;
    }
    File newFile = new File(toBeRenamed.getParent(), newFilename);
    return toBeRenamed.renameTo(newFile);
  }
}
