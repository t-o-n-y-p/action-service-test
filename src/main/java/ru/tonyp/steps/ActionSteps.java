package ru.tonyp.steps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import ru.tonyp.client.EndpointClient;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointResponse;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class ActionSteps {

  private final EndpointClient endpointClient;

  @Step("Отправить запрос на выполнение действия")
  public EndpointResponse sendEndpointRequest(EndpointRequest request) {
    return endpointClient.endpoint(request, HttpStatus.SC_OK);
  }

  @Step("Отправить запрос на выполнение действия с ожидаемым кодом ответа {statusCode}")
  public EndpointResponse sendEndpointRequest(EndpointRequest request, int statusCode) {
    return endpointClient.endpoint(request, statusCode);
  }

  @Step("Отправить запрос на выполнение действия с ожидаемым кодом ответа {statusCode}")
  public EndpointResponse sendEndpointRequest(
      String apiKey, EndpointRequest request, int statusCode) {
    return endpointClient.endpoint(apiKey, request, statusCode);
  }
}
