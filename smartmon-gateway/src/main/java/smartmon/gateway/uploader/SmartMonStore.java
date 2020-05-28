package smartmon.gateway.uploader;

import org.springframework.http.codec.multipart.FilePart;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.utilities.general.SmartMonResponse;

public interface SmartMonStore {
  SmartMonResponse<SmartMonStoreFile> put(String type, FilePart file);
}
