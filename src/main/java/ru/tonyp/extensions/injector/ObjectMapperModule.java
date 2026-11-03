package ru.tonyp.extensions.injector;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.restassured.internal.mapping.Jackson3Mapper;
import tools.jackson.databind.json.JsonMapper;

@SuppressWarnings("unused")
public class ObjectMapperModule extends AbstractModule {

  @Provides
  @Singleton
  private JsonMapper jsonMapper() {
    return JsonMapper.builder()
        .changeDefaultPropertyInclusion(i -> i.withValueInclusion(JsonInclude.Include.NON_NULL))
        .build();
  }

  @Provides
  @Singleton
  private Jackson3Mapper jsonJackson3Mapper(JsonMapper jsonMapper) {
    return new Jackson3Mapper((cls, charset) -> jsonMapper);
  }
}
