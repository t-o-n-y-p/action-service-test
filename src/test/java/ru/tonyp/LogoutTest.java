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
import ru.tonyp.extensions.annotations.RevokedToken;
import ru.tonyp.steps.ActionSteps;
import ru.tonyp.steps.AssertionSteps;
import ru.tonyp.steps.PreparationSteps;

@SuppressWarnings("unused")
@Epic("Запрос на выполнение действия")
@Feature("Действие LOGOUT")
public class LogoutTest {

  @Inject private ActionSteps actionSteps;
  @Inject private PreparationSteps preparationSteps;
  @Inject private AssertionSteps assertionSteps;

  @Test
  @DisplayName("Выход с токеном, прошедшим авторизацию")
  void logoutWithValidTokenAndAuth(@ActionSuccessToken String token) {
    EndpointRequest request =
        EndpointRequest.builder().token(token).action(EndpointRequestAction.LOGOUT.name()).build();
    EndpointResponse actual = actionSteps.sendEndpointRequest(request);
    EndpointResponse expected = preparationSteps.prepareSuccessfulEndpointResponse();
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @Test
  @DisplayName("Выход с токеном, прошедшим авторизацию и выход")
  void logoutWithRevokedToken(@RevokedToken String token) {
    EndpointRequest request =
        EndpointRequest.builder().token(token).action(EndpointRequestAction.LOGOUT.name()).build();
    EndpointResponse actual = actionSteps.sendEndpointRequest(request, HttpStatus.SC_FORBIDDEN);
    EndpointResponse expected = preparationSteps.prepareUnauthorizedTokenEndpointResponse(request);
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @Test
  @DisplayName("Выход с токеном, не прошедшим авторизацию")
  void logoutWithValidTokenNoAuth() {
    EndpointRequest request =
        preparationSteps.prepareRequestWithRandomToken(EndpointRequestAction.ACTION);
    EndpointResponse actual = actionSteps.sendEndpointRequest(request, HttpStatus.SC_FORBIDDEN);
    EndpointResponse expected = preparationSteps.prepareUnauthorizedTokenEndpointResponse(request);
    assertionSteps.assertEndpointResponse(actual, expected);
  }
}
