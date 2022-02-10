package vip.zhouxin.ureport.core.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.chart.dataset.impl.BubbleDataset;
import vip.zhouxin.ureport.core.chart.dataset.impl.MixDataset;
import vip.zhouxin.ureport.core.chart.dataset.impl.ScatterDataset;
import vip.zhouxin.ureport.core.chart.dataset.impl.category.*;

/**
 * @author xinxingzhou
 * @since 2022/2/1
 */
@Configuration
public class DatasetConfiguration {
  @Bean
  public NamedType areaNamedType() {
    return new NamedType(AreaDataset.class, AreaDataset.TYPE);
  }
  @Bean
  public NamedType barNamedType() {
    return new NamedType(BarDataset.class, BarDataset.TYPE);
  }
  @Bean
  public NamedType doughnutNamedType() {
    return new NamedType(DoughnutDataset.class, DoughnutDataset.TYPE);
  }
  @Bean
  public NamedType horizontalBarNamedType() {
    return new NamedType(HorizontalBarDataset.class, HorizontalBarDataset.TYPE);
  }
  @Bean
  public NamedType lineNamedType() {
    return new NamedType(LineDataset.class, LineDataset.TYPE);
  }
  @Bean
  public NamedType pieNamedType() {
    return new NamedType(PieDataset.class, PieDataset.TYPE);
  }
  @Bean
  public NamedType polarNamedType() {
    return new NamedType(PolarDataset.class, PolarDataset.TYPE);
  }
  @Bean
  public NamedType radarNamedType() {
    return new NamedType(RadarDataset.class, RadarDataset.TYPE);
  }
  @Bean
  public NamedType bubbleNamedType() {
    return new NamedType(BubbleDataset.class, BubbleDataset.TYPE);
  }
  @Bean
  public NamedType mixNamedType() {
    return new NamedType(MixDataset.class, MixDataset.TYPE);
  }
  @Bean
  public NamedType scatterNamedType() {
    return new NamedType(ScatterDataset.class, ScatterDataset.TYPE);
  }
}
