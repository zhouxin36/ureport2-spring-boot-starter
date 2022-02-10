package vip.zhouxin.ureport.core.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.definition.searchform.*;

/**
 * @author xinxingzhou
 * @since 2022/2/1
 */
@Configuration
public class ComponentConfiguration {

  @Bean
  public NamedType checkboxNamedType() {
    return new NamedType(CheckboxInputComponent.class, CheckboxInputComponent.TYPE);
  }

  @Bean
  public NamedType datetimeNamedType() {
    return new NamedType(DateInputComponent.class, DateInputComponent.TYPE);
  }

  @Bean
  public NamedType grid1NamedType() {
    return new NamedType(GridComponent.class, GridComponent.TYPE1);
  }

  @Bean
  public NamedType grid2NamedType() {
    return new NamedType(GridComponent.class, GridComponent.TYPE2);
  }

  @Bean
  public NamedType grid3NamedType() {
    return new NamedType(GridComponent.class, GridComponent.TYPE3);
  }

  @Bean
  public NamedType grid4NamedType() {
    return new NamedType(GridComponent.class, GridComponent.TYPE4);
  }

  @Bean
  public NamedType grid5NamedType() {
    return new NamedType(GridComponent.class, GridComponent.TYPE5);
  }

  @Bean
  public NamedType radioNamedType() {
    return new NamedType(RadioInputComponent.class, RadioInputComponent.TYPE);
  }

  @Bean
  public NamedType resetNamedType() {
    return new NamedType(ResetButtonComponent.class, ResetButtonComponent.TYPE);
  }

  @Bean
  public NamedType selectNamedType() {
    return new NamedType(SelectInputComponent.class, SelectInputComponent.TYPE);
  }

  @Bean
  public NamedType submitNamedType() {
    return new NamedType(SubmitButtonComponent.class, SubmitButtonComponent.TYPE);
  }

  @Bean
  public NamedType colsNamedType() {
    return new NamedType(ColComponent.class, ColComponent.TYPE);
  }

  @Bean
  public NamedType textNamedType() {
    return new NamedType(TextInputComponent.class, TextInputComponent.TYPE);
  }

}
