package vip.zhouxin.ureport.core.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.definition.dataset.BeanDatasetDefinition;
import vip.zhouxin.ureport.core.definition.dataset.SqlDatasetDefinition;
import vip.zhouxin.ureport.core.definition.datasource.BuildinDatasourceDefinition;
import vip.zhouxin.ureport.core.definition.datasource.JdbcDatasourceDefinition;
import vip.zhouxin.ureport.core.definition.datasource.SpringBeanDatasourceDefinition;

/**
 * @author xinxingzhou
 * @since 2022/2/1
 */
@Configuration
public class DefinitionConfiguration {

  @Bean
  public NamedType jdbcNamedType() {
    return new NamedType(JdbcDatasourceDefinition.class, JdbcDatasourceDefinition.TYPE);
  }
  @Bean
  public NamedType buildinNamedType() {
    return new NamedType(BuildinDatasourceDefinition.class, BuildinDatasourceDefinition.TYPE);
  }
  @Bean
  public NamedType springNamedType() {
    return new NamedType(SpringBeanDatasourceDefinition.class, SpringBeanDatasourceDefinition.TYPE);
  }
  @Bean
  public NamedType beanDataNamedType() {
    return new NamedType(BeanDatasetDefinition.class, BeanDatasetDefinition.TYPE);
  }
  @Bean
  public NamedType sqlDataNamedType() {
    return new NamedType(SqlDatasetDefinition.class, SqlDatasetDefinition.TYPE);
  }
}
