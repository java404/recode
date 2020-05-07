package smartmon.webdata.datasource;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class SmartMonDataSource {
  @Value("${smartmon.datasource.standalone:true}")
  private boolean standalone;

  @Value("${smartmon.datasource.mysql.host:127.0.0.1}")
  private String mysqlHost;

  @Value("${smartmon.datasource.mysql.port:3306}")
  private String mysqlPort;

  @Value("${smartmon.datasource.mysql.user:smartmon}")
  private String mysqlUser;

  @Value("${smartmon.datasource.mysql.db:}")
  private String mysqlDb;

  @Value("${smartmon.datasource.mysql.password:smartmon123}")
  private String mysqlPassword;

  @Value("${smartmon.datasource.h2.file:/var/smartmon/smartmon.h2}")
  private String h2DataFile;

  @Value("${smartmon.datasource.h2.user:smartmon}")
  private String h2Username;

  @Value("${smartmon.datasource.h2.password:smartmon123}")
  private String h2Password;

  @Value("${smartmon.datasource.url:}")
  private String jdbcUrl;

  @Value("${smartmon.datasource.liquibase.changeLog:}")
  private String liquibaseChangeLog;

  @Value("${smartmon.datasource.liquibase.deaultSchema:PUBLIC}")
  private String liquibaseSchema;

  @Value("${smartmon.datasource.liquibase.debug:false}")
  private boolean liquibaseDebug;

  @ConfigurationProperties(prefix = "spring.datasource")
  @Primary
  @Bean
  public DataSource getDataSource() {
    return standalone ? makeStandaloneDataSource() : makeMySqlDataSource();
  }

  @Bean
  public SpringLiquibase liquibase() {
    log.info("Liquibase changelog: {}.", Strings.nullToEmpty(liquibaseChangeLog));
    final SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setDataSource(getDataSource());
    liquibase.setChangeLog(liquibaseChangeLog);
    if (standalone) {
      // MySQL does not support the db schema.
      liquibase.setDefaultSchema(Strings.nullToEmpty(liquibaseSchema));
    }
    liquibase.setShouldRun(!Strings.isNullOrEmpty(liquibaseChangeLog));
    final Map<String, String> params = Maps.newHashMap();
    if (liquibaseDebug) {
      params.put("verbose", "true");
      liquibase.setChangeLogParameters(params);
    }
    return liquibase;
  }

  private String makeH2Url() {
    log.info("Make jdbc url for h2 ({})", Strings.nullToEmpty(h2DataFile));
    return String.format("jdbc:h2:file:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", h2DataFile);
  }

  private DataSource makeStandaloneDataSource() {
    log.info("DataSource is standalone mode. (H2)");
    final String url = Strings.isNullOrEmpty(jdbcUrl) ? makeH2Url() : jdbcUrl;
    log.debug("DataSource Url: {}", url);
    return DataSourceBuilder.create().type(HikariDataSource.class)
      .url(url).username(h2Username).password(h2Password).build();
  }

  private String makeMySqlUrl() {
    log.info("Make jdbc url for mysql ({}:{} - {} - {})",
      Strings.nullToEmpty(mysqlHost), Strings.nullToEmpty(mysqlPort),
      Strings.nullToEmpty(mysqlDb), Strings.nullToEmpty(mysqlUser));

    final StringBuilder sb = new StringBuilder();
    sb.append("jdbc:mysql://").append(mysqlHost).append(":").append(mysqlPort).append("/").append(mysqlDb)
      .append("?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull");
    return sb.toString();
  }

  private DataSource makeMySqlDataSource() {
    log.info("DataSource is server mode. (MySQL)");
    final String url = Strings.isNullOrEmpty(jdbcUrl) ? makeMySqlUrl() : jdbcUrl;
    return DataSourceBuilder.create().type(HikariDataSource.class)
      .url(url).username(mysqlUser).password(mysqlPassword).build();
  }
}