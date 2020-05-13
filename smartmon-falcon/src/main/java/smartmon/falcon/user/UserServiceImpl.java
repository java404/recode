package smartmon.falcon.user;

import java.util.List;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.user.FalconUser;
import smartmon.falcon.remote.types.user.FalconUserCreateParam;
import smartmon.falcon.remote.types.user.FalconUserDeleteParam;
import smartmon.falcon.remote.types.user.FalconUserUpdateParam;
import smartmon.falcon.user.command.UserCreateCommand;
import smartmon.falcon.user.command.UserUpdateCommand;
import smartmon.utilities.misc.BeanConverter;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private FalconApiComponent falconApiComponent;

  @Override
  public List<User> getUserList() {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final List<FalconUser> falconUsers = falconClient.listUsers(falconApiComponent.getApiToken());
    return ListUtils.emptyIfNull(BeanConverter.copy(falconUsers, User.class));
  }

  @Override
  public void createUser(UserCreateCommand createCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    falconClient.createUser(BeanConverter.copy(createCommand,
      FalconUserCreateParam.class), falconApiComponent.getApiToken());
  }

  @Override
  public void updateUser(UserUpdateCommand updateCommand) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    falconClient.updateUser(BeanConverter.copy(updateCommand,
      FalconUserUpdateParam.class), falconApiComponent.getApiToken());
  }

  @Override
  public void deleteUser(Integer id) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final FalconUserDeleteParam userDeleteParam = new FalconUserDeleteParam(id);
    falconClient.deleteUser(userDeleteParam, falconApiComponent.getApiToken());
  }
}
