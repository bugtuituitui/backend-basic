package com.example.common.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

  // 脱敏类型
  private SensitiveStrategy strategy;
  // 前几位不脱敏
  private Integer prefixNoMaskLen;
  // 最后几位不脱敏
  private Integer suffixNoMaskLen;
  // 用什么打码
  private String symbol;

  @Override
  public void serialize(final String origin, final JsonGenerator jsonGenerator,
      final SerializerProvider serializerProvider) throws IOException {
    if (StringUtils.isNotBlank(origin) && null != strategy) {
      switch (strategy) {
        case CUSTOMER:
          jsonGenerator.writeString(SensitiveUtil.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, symbol));
          break;
        case NAME:
          jsonGenerator.writeString(SensitiveUtil.hideChineseName(origin));
          break;
        case ID_CARD:
          jsonGenerator.writeString(SensitiveUtil.hideIDCard(origin));
          break;
        case PHONE:
          jsonGenerator.writeString(SensitiveUtil.hidePhone(origin));
          break;
        case EMAIL:
          jsonGenerator.writeString(SensitiveUtil.hideEmail(origin));
          break;
        default:
          throw new IllegalArgumentException("unknown privacy type enum " + strategy);
      }
    }
  }

  @Override
  public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
      final BeanProperty beanProperty) throws JsonMappingException {
    if (beanProperty != null) {
      if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
        Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
        if (sensitive == null) {
          sensitive = beanProperty.getContextAnnotation(Sensitive.class);
        }
        if (sensitive != null) {
          return new SensitiveSerializer(sensitive.strategy(), sensitive.prefixNoMaskLen(),
              sensitive.suffixNoMaskLen(), sensitive.symbol());
        }
      }
      return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
    return serializerProvider.findNullValueSerializer(null);
  }
}
