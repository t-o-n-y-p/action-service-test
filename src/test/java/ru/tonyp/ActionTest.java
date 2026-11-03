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
import ru.tonyp.extensions.annotations.ActionErrorToken;
import ru.tonyp.extensions.annotations.ActionSuccessToken;
import ru.tonyp.steps.ActionSteps;
import ru.tonyp.steps.AssertionSteps;
import ru.tonyp.steps.PreparationSteps;

@SuppressWarnings("unused")
@Epic("Запрос на выполнение действия")
@Feature("Действие ACTION")
public class ActionTest {

  @Inject private ActionSteps actionSteps;
  @Inject private PreparationSteps preparationSteps;
  @Inject private AssertionSteps assertionSteps;

  @Test
  @DisplayName("Действие с корректным токеном, успешный ответ внешнего сервиса")
  void actionWithValidTokenAndValidExternalServiceResponse(@ActionSuccessToken String token) {
    EndpointRequest request =
        EndpointRequest.builder().token(token).action(EndpointRequestAction.ACTION.name()).build();
    EndpointResponse actual = actionSteps.sendEndpointRequest(request);
    EndpointResponse expected = preparationSteps.prepareSuccessfulEndpointResponse();
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @Test
  @DisplayName("Действие с корректным токеном, ошибочный ответ внешнего сервиса")
  void actionWithValidTokenAndInvalidExternalServiceResponse(@ActionErrorToken String token) {
    EndpointRequest request =
        EndpointRequest.builder().token(token).action(EndpointRequestAction.ACTION.name()).build();
    EndpointResponse actual =
        actionSteps.sendEndpointRequest(request, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    EndpointResponse expected = preparationSteps.prepareInternalServerErrorEndpointResponse();
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @Test
  @DisplayName("Действие с токеном, не прошедшим авторизацию")
  void actionWithValidTokenNoAuth() {
    EndpointRequest request =
        preparationSteps.prepareRequestWithRandomToken(EndpointRequestAction.ACTION);
    EndpointResponse actual = actionSteps.sendEndpointRequest(request, HttpStatus.SC_FORBIDDEN);
    EndpointResponse expected = preparationSteps.prepareUnauthorizedTokenEndpointResponse(request);
    assertionSteps.assertEndpointResponse(actual, expected);
  }
}
