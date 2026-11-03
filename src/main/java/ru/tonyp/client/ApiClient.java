package ru.tonyp.client;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.tonyp.config.SystemProperties;

public abstract class ApiClient {

  protected final String apiKeyHeader = "X-Api-Key";

  protected abstract String getBasePath();

  protected abstract ContentType getRequestContentType();

  protected abstract ContentType getResponseContentType();

  protected RequestSpecification getRequestSpecification(
      SystemProperties systemProperties, String apiKey) {
    RequestSpecification requestSpecification = getRequestSpecification(systemProperties);
    return apiKey == null
        ? requestSpecification
        : requestSpecification.header(apiKeyHeader, apiKey);
  }

  private RequestSpecification getRequestSpecification(SystemProperties systemProperties) {
    return given()
        .baseUri(systemProperties.getBaseUrl())
        .basePath(getBasePath())
        .contentType(getRequestContentType())
        .accept(getResponseContentType())
        .log()
        .all()
        .filter(
            new AllureRestAssured()
                .configureHttpExchange(exchange -> exchange.redactHeader(apiKeyHeader)));
  }

  protected ResponseSpecification getResponseSpecification(int statusCode) {
    return expect()
        .logDetail(LogDetail.ALL)
        .onFailMessage("Код ответа должен соответствовать ожидаемому")
        .statusCode(equalTo(statusCode))
        .onFailMessage("Content-Type должен соответствовать ожидаемому")
        .contentType(getResponseContentType());
  }
}
