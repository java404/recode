package smartmon.falcon.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import smartmon.falcon.entity.NodeConfigEntity;
import smartmon.falcon.mapper.NodeConfigMapper;
import tk.mybatis.mapper.entity.Example;

@Service
public class NodeConfigService {
  @Autowired
  private NodeConfigMapper nodeConfigMapper;

  public List<NodeConfigEntity> getAll(String hostname, String name) {
    Example example = new Example(NodeConfigEntity.class);
    Example.Criteria criteria = example.createCriteria();
    if (!StringUtils.isEmpty(hostname)) {
      criteria.andEqualTo("hostname", hostname);
    }
    if (!StringUtils.isEmpty(name)) {
      criteria.andEqualTo("name", name);
    }
    return nodeConfigMapper.selectByExample(example);
  }

  public NodeConfigEntity addNodeConfig(String data, String hostname, String name) {
    String uuid = UUID.randomUUID().toString();
    // on duplicate too much increase of id
    // nodeConfigMapper.saveOrUpdate(uuid, data, hostname, name);
    nodeConfigMapper.update(data, hostname, name);
    nodeConfigMapper.save(uuid, data, hostname, name);
    NodeConfigEntity nodeConfigEntity = new NodeConfigEntity();
    nodeConfigEntity.setHostname(hostname);
    nodeConfigEntity.setName(name);
    return nodeConfigEntity;
  }

  public void deleteNodeConfig(Integer id) {
    nodeConfigMapper.deleteById(id);
  }
}
