package smartmon.injector.upload;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

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
    FileChannel fileChannel = tempRaf.getChannel();
    long offset = UploadConstants.CHUNK_SIZE * param.getChunk();
    byte[] fileData = param.getBytes();
    MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
    mappedByteBuffer.put(fileData);
    freedMappedByteBuffer(mappedByteBuffer);
    fileChannel.close();
    boolean isOk = checkAndSetUploadProgress(param, filepath);
    if (isOk) {
      boolean flag = renameFile(tmpFile, fileName);
      log.info("upload complete " + flag + " name=" + fileName);
      flag = tmpFile.setExecutable(true);
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

  public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
    try {
      if (mappedByteBuffer == null) {
        return;
      }
      mappedByteBuffer.force();
      AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
        try {
          Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner");
          getCleanerMethod.setAccessible(true);
          sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer);
          cleaner.clean();
        } catch (Exception e) {
          log.error("clean MappedByteBuffer error!", e);
        }
        log.info("clean MappedByteBuffer completed!");
        return null;
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
