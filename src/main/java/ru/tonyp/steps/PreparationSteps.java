package ru.tonyp.steps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointRequestAction;
import ru.tonyp.dto.EndpointResponse;
import ru.tonyp.dto.EndpointResponseResult;
import ru.tonyp.utils.FakerUtils;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class PreparationSteps {

  private final FakerUtils fakerUtils;

  @Step("Подготовить тело запроса со случайным токеном")
  public EndpointRequest prepareRequestWithRandomToken(EndpointRequestAction action) {
    return prepareRequestWithRandomToken(action.name());
  }

  @Step("Подготовить тело запроса со случайным токеном")
  public EndpointRequest prepareRequestWithRandomToken(String action) {
    return EndpointRequest.builder().token(fakerUtils.getToken()).action(action).build();
  }

  @Step("Подготовить тело запроса с токеном, вызывающим ошибку авторизации на внешнем сервисе")
  public EndpointRequest prepareRequestWithRandomAuthErrorToken() {
    return EndpointRequest.builder()
        .token(fakerUtils.getAuthErrorToken())
        .action(EndpointRequestAction.LOGIN.name())
        .build();
  }

  @Step("Подготовить тело запроса с токеном, вызывающим ошибку действия на внешнем сервисе")
  public EndpointRequest prepareRequestWithRandomActionErrorToken() {
    return EndpointRequest.builder()
        .token(fakerUtils.getActionErrorToken())
        .action(EndpointRequestAction.LOGIN.name())
        .build();
  }

  @Step("Подготовить тело успешного ответа")
  public EndpointResponse prepareSuccessfulEndpointResponse() {
    return EndpointResponse.builder().result(EndpointResponseResult.OK).build();
  }

  @Step("Подготовить тело ответа с ошибкой сервера")
  public EndpointResponse prepareInternalServerErrorEndpointResponse() {
    return EndpointResponse.builder()
        .result(EndpointResponseResult.ERROR)
        .message("Internal Server Error")
        .build();
  }

  @Step("Подготовить тело ответа на запрос с невалидным токеном")
  public EndpointResponse prepareInvalidTokenEndpointResponse(EndpointRequest request) {
    String message =
        request.token() == null
            ? "token: must not be null"
            : "token: must match \"^[0-9A-F]{32}$\"";
    return EndpointResponse.builder().result(EndpointResponseResult.ERROR).message(message).build();
  }

  @Step("Подготовить тело ответа с невалидным действием")
  public EndpointResponse prepareInvalidActionEndpointResponse(String action) {
    String errorMessageValue = String.valueOf(action == null || action.isBlank() ? null : action);
    return EndpointResponse.builder()
        .result(EndpointResponseResult.ERROR)
        .message(
            "action: invalid action '%s'. Allowed: LOGIN, LOGOUT, ACTION"
                .formatted(errorMessageValue))
        .build();
  }

  @Step("Подготовить тело ответа на запрос с токеном без авторизации")
  public EndpointResponse prepareUnauthorizedTokenEndpointResponse(EndpointRequest request) {
    return EndpointResponse.builder()
        .result(EndpointResponseResult.ERROR)
        .message("Token '%s' not found".formatted(request.token()))
        .build();
  }

  @Step("Подготовить тело ответа на запрос повторной авторизации токена")
  public EndpointResponse prepareAuthorizedTokenEndpointResponse(EndpointRequest request) {
    return EndpointResponse.builder()
        .result(EndpointResponseResult.ERROR)
        .message("Token '%s' already exists".formatted(request.token()))
        .build();
  }

  @Step("Подготовить тело ответа на запрос с невалидным API ключом")
  public EndpointResponse prepareInvalidApiKeyEndpointResponse() {
    return EndpointResponse.builder()
        .result(EndpointResponseResult.ERROR)
        .message("Missing or invalid API Key")
        .build();
  }
}
