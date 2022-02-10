package vip.zhouxin.ureport.core.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.expression.model.condition.BothExpressionCondition;
import vip.zhouxin.ureport.core.expression.model.condition.CellExpressionCondition;
import vip.zhouxin.ureport.core.expression.model.condition.CurrentValueExpressionCondition;
import vip.zhouxin.ureport.core.expression.model.condition.PropertyExpressionCondition;

/**
 * @author xinxingzhou
 * @since 2022/2/1
 */
@Configuration
public class ConditionConfiguration {

  @Bean
  public NamedType bothExpressionNamedType() {
    return new NamedType(BothExpressionCondition.class, BothExpressionCondition.TYPE);
  }

  @Bean
  public NamedType cellExpressionNamedType() {
    return new NamedType(CellExpressionCondition.class, CellExpressionCondition.TYPE);
  }

  @Bean
  public NamedType currentValueExpressionNamedType() {
    return new NamedType(CurrentValueExpressionCondition.class, CurrentValueExpressionCondition.TYPE);
  }

  @Bean
  public NamedType propertyExpressionConditionNamedType() {
    return new NamedType(PropertyExpressionCondition.class, PropertyExpressionCondition.TYPE);
  }
}
