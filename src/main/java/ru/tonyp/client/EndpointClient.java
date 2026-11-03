package ru.tonyp.client;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.restassured.http.ContentType;
import io.restassured.internal.mapping.Jackson3Mapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import ru.tonyp.config.SystemProperties;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointResponse;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class EndpointClient extends ApiClient {

  private final JsonMapper requestBodyMapper;
  private final Jackson3Mapper responseBodyMapper;
  private final SystemProperties systemProperties;

  @Override
  protected String getBasePath() {
    return "/endpoint";
  }

  @Override
  protected ContentType getRequestContentType() {
    return ContentType.URLENC;
  }

  @Override
  protected ContentType getResponseContentType() {
    return ContentType.JSON;
  }

  public EndpointResponse endpoint(EndpointRequest request, int statusCode) {
    return endpoint(systemProperties.getApiKey(), request, statusCode);
  }

  public EndpointResponse endpoint(String apiKey, EndpointRequest request, int statusCode) {
    return given(getRequestSpecification(systemProperties, apiKey))
        .formParams(
            requestBodyMapper.convertValue(request, new TypeReference<Map<String, Object>>() {}))
        .when()
        .post()
        .then()
        .spec(getResponseSpecification(statusCode))
        .onFailMessage("Ответ сервера должен соответствовать схеме")
        .body(matchesJsonSchemaInClasspath("schema/endpoint.json"))
        .extract()
        .as(EndpointResponse.class, responseBodyMapper);
  }
}
