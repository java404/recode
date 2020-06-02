package smartmon.falcon.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * @author lgy
 * @since 2020/05/19
 */
@JsonPropertyOrder({ "id", "uuid", "data", "hostname", "name", "createtime", "updatetime" })
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "node_config")
public class NodeConfigEntity {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("uuid")
  private String uuid;

  @JsonProperty("data")
  @JsonRawValue
  private String data;

  @JsonProperty("name")
  private String name;

  @JsonProperty("hostname")
  private String hostname;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @JsonProperty("createtime")
  @Column(name = "createtime")
  private Date createTime;

  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @JsonProperty("updatetime")
  @Column(name = "updatetime")
  private Date updateTime;

  /**
   * @return the iD
   */
  public Integer getId() {
    return id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * @param iD the iD to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
