package smartmon.injector.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;

class FileUploader {
  static boolean upLoadFiles(String uploadUrl, Map<String, File> files) throws IOException {
    if (MapUtils.isEmpty(files)) {
      return false;
    }
    String boundary = java.util.UUID.randomUUID().toString();
    String prefix = "--";
    String linend = "\r\n";
    String multipartFromData = "multipart/form-data";
    String charset = "UTF-8";
    URL uri = new URL(uploadUrl);
    HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
    conn.setReadTimeout(5 * 1000);
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setUseCaches(false);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("connection", "keep-alive");
    conn.setRequestProperty("Charsert", "UTF-8");
    conn.setRequestProperty("Content-Type", multipartFromData + ";boundary=" + boundary);
    try (DataOutputStream outStream = new DataOutputStream(conn.getOutputStream())) {
      for (Map.Entry<String, File> file : files.entrySet()) {
        String parameters = prefix + boundary + linend
          + "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + linend
          + "Content-Type: application/octet-stream; charset=" + charset + linend + linend;
        outStream.write(parameters.getBytes());
        InputStream is = new FileInputStream(file.getValue());
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
          outStream.write(buffer, 0, len);
        }
        is.close();
        outStream.write(linend.getBytes());
      }
      byte[] endData = (prefix + boundary + prefix + linend).getBytes();
      outStream.write(endData);
      outStream.flush();
      int res = conn.getResponseCode();
      return Objects.equals(HttpStatus.OK.value(), res);
    } finally {
      conn.disconnect();
    }
  }
}
