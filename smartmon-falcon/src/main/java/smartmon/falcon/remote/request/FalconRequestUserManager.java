package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.user.FalconUser;
import smartmon.falcon.remote.types.user.FalconUserCreateParam;
import smartmon.falcon.remote.types.user.FalconUserDeleteParam;
import smartmon.falcon.remote.types.user.FalconUserUpdateParam;

@Headers({"Content-Type: application/json"})
public interface FalconRequestUserManager {
  @RequestLine("GET /user/users")
  List<FalconUser> listUsers(@HeaderMap Map<String, String> falconApiToken);

  @RequestLine("POST /user/create")
  FalconResponseData createUser(@RequestBody FalconUserCreateParam createParam,
                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("PUT /admin/change_user_profile")
  FalconResponseData updateUser(@RequestBody FalconUserUpdateParam updateParam,
                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("DELETE /admin/delete_user")
  FalconResponseData deleteUser(@RequestBody FalconUserDeleteParam deleteParam,
                                @HeaderMap Map<String, String> falconApiToken);


}
