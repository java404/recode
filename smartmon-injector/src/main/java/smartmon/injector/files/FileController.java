package smartmon.injector.files;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/files")
@RestController
public class FileController {
  @Autowired
  private FileService fileService;

  @PostMapping("upload")
  public void upload(HttpServletRequest request) throws Exception {
    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
    Map<String, List<MultipartFile>> fileMap = multiRequest.getMultiFileMap();
    for (List<MultipartFile> files : fileMap.values()) {
      for (MultipartFile file : files) {
        fileService.saveFile(file);
      }
    }
  }
}
