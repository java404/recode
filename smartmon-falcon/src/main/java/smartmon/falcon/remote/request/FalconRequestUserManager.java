package smartmon.falcon.remote.request;

import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.user.FalconUser;
import smartmon.falcon.remote.types.user.FalconUserCreateParam;
import smartmon.falcon.remote.types.user.FalconUserDeleteParam;
import smartmon.falcon.remote.types.user.FalconUserUpdateParam;

import java.util.List;

public interface FalconRequestUserManager {
  @RequestLine("GET /user/users")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  List<FalconUser> listUsers();

  @RequestLine("POST /user/create")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  FalconResponseData createUser(@RequestBody FalconUserCreateParam createParam);

  @RequestLine("PUT /admin/change_user_profile")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  FalconResponseData updateUser(@RequestBody FalconUserUpdateParam updateParam);

  @RequestLine("DELETE /admin/delete_user")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  FalconResponseData deleteUser(@RequestBody FalconUserDeleteParam deleteParam);


}
