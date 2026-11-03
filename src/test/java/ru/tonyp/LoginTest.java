package ru.tonyp;

import com.google.inject.Inject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointRequestAction;
import ru.tonyp.dto.EndpointResponse;
import ru.tonyp.extensions.annotations.ActionSuccessToken;
import ru.tonyp.steps.ActionSteps;
import ru.tonyp.steps.AssertionSteps;
import ru.tonyp.steps.PreparationSteps;

@SuppressWarnings("unused")
@Epic("Запрос на выполнение действия")
@Feature("Действие LOGIN")
public class LoginTest {

  @Inject private ActionSteps actionSteps;
  @Inject private PreparationSteps preparationSteps;
  @Inject private AssertionSteps assertionSteps;

  @Test
  @DisplayName("Логин с корректным токеном, успешный ответ внешнего сервиса")
  void loginWithValidTokenAndValidExternalServiceResponse() {
    EndpointRequest request =
        preparationSteps.prepareRequestWithRandomToken(EndpointRequestAction.LOGIN);
    EndpointResponse actual = actionSteps.sendEndpointRequest(request);
    EndpointResponse expected = preparationSteps.prepareSuccessfulEndpointResponse();
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @Test
  @DisplayName("Логин с корректным токеном, который уже авторизован")
  void loginWithAuthorizedValidToken(@ActionSuccessToken String token) {
    EndpointRequest request =
        EndpointRequest.builder().token(token).action(EndpointRequestAction.LOGIN.name()).build();
    EndpointResponse actual = actionSteps.sendEndpointRequest(request, HttpStatus.SC_CONFLICT);
    EndpointResponse expected = preparationSteps.prepareAuthorizedTokenEndpointResponse(request);
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @Test
  @DisplayName("Логин с корректным токеном, ошибочный ответ внешнего сервиса")
  void loginWithValidTokenAndInvalidExternalServiceResponse() {
    EndpointRequest request = preparationSteps.prepareRequestWithRandomAuthErrorToken();
    EndpointResponse actual =
        actionSteps.sendEndpointRequest(request, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    EndpointResponse expected = preparationSteps.prepareInternalServerErrorEndpointResponse();
    assertionSteps.assertEndpointResponse(actual, expected);
  }
}
