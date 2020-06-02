package smartmon.gateway.uploader;

import org.springframework.http.codec.multipart.FilePart;
import smartmon.core.store.SmartMonStoreFile;

public interface SmartMonStore {
  SmartMonStoreFile put(String type, String desc, FilePart file);
}
