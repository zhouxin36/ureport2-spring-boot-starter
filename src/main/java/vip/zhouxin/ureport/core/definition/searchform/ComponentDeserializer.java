package vip.zhouxin.ureport.core.definition.searchform;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Component 解码器
 *
 * @author xinxingzhou
 * @since 2022/1/27
 */
public class ComponentDeserializer extends JsonDeserializer<Component> {

  private final Map<String, Class<?>> parsers;
  private final ObjectMapper objectMapper;

  public ComponentDeserializer(Map<String, Class<?>> parsers) {
    this.parsers = parsers;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Component deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    Map map = p.readValueAs(Map.class);
    if (map == null) {
      return null;
    }
    Class clazz = parsers.get(map.getOrDefault("type", ""));
    if (clazz == null) {
      return null;
    }
    return (Component) objectMapper.readValue(objectMapper.writeValueAsString(map), clazz);
  }

  @Override
  public Class<?> handledType() {
    return Component.class;
  }
}
