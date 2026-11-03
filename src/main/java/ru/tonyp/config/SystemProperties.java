package ru.tonyp.config;

import com.google.inject.Singleton;
import lombok.Getter;

@Singleton
@Getter
public class SystemProperties {

  private final String baseUrl = System.getProperty("baseUrl");
  private final String apiKey = System.getProperty("apiKey");
}
