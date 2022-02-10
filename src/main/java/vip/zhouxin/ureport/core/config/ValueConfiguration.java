package vip.zhouxin.ureport.core.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.definition.value.*;

/**
 * @author xinxingzhou
 * @since 2022/2/1
 */
@Configuration
public class ValueConfiguration {
  @Bean
  public NamedType chartNamedType() {
    return new NamedType(ChartValue.class, ChartValue.TYPE);
  }
  @Bean
  public NamedType datasetNamedType() {
    return new NamedType(DatasetValue.class, DatasetValue.TYPE);
  }
  @Bean
  public NamedType expressionNamedType() {
    return new NamedType(ExpressionValue.class, ExpressionValue.TYPE);
  }
  @Bean
  public NamedType imageNamedType() {
    return new NamedType(ImageValue.class, ImageValue.TYPE);
  }
  @Bean
  public NamedType simpleNamedType() {
    return new NamedType(SimpleValue.class, SimpleValue.TYPE);
  }
  @Bean
  public NamedType slashNamedType() {
    return new NamedType(SlashValue.class, SlashValue.TYPE);
  }
  @Bean
  public NamedType zxingNamedType() {
    return new NamedType(ZxingValue.class, ZxingValue.TYPE);
  }

}
