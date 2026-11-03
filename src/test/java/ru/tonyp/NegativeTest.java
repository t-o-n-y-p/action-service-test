package ru.tonyp;

import com.google.inject.Inject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.Arrays;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointRequestAction;
import ru.tonyp.dto.EndpointResponse;
import ru.tonyp.steps.ActionSteps;
import ru.tonyp.steps.AssertionSteps;
import ru.tonyp.steps.PreparationSteps;

@SuppressWarnings("unused")
@Epic("Запрос на выполнение действия")
@Feature("Негативные тесты")
public class NegativeTest {

  @Inject private ActionSteps actionSteps;
  @Inject private PreparationSteps preparationSteps;
  @Inject private AssertionSteps assertionSteps;

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidActions")
  @DisplayName("Запрос с некорректным действием")
  @Story("Проверка действия")
  void requestWithInvalidAction(String action) {
    EndpointRequest request = preparationSteps.prepareRequestWithRandomToken(action);
    EndpointResponse actual = actionSteps.sendEndpointRequest(request, HttpStatus.SC_BAD_REQUEST);
    EndpointResponse expected = preparationSteps.prepareInvalidActionEndpointResponse(action);
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidTokens")
  @DisplayName("Запрос с некорректным токеном")
  @Story("Проверка токена")
  void requestWithInvalidToken(EndpointRequest request) {
    EndpointResponse actual = actionSteps.sendEndpointRequest(request, HttpStatus.SC_BAD_REQUEST);
    EndpointResponse expected = preparationSteps.prepareInvalidTokenEndpointResponse(request);
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidApiKeys")
  @DisplayName("Запрос с некорректным API ключом")
  @Story("Проверка API ключа")
  void requestWithInvalidApiKey(ApiKeyWithAction data) {
    EndpointRequest request = preparationSteps.prepareRequestWithRandomToken(data.action());
    EndpointResponse actual =
        actionSteps.sendEndpointRequest(data.apiKey(), request, HttpStatus.SC_UNAUTHORIZED);
    EndpointResponse expected = preparationSteps.prepareInvalidApiKeyEndpointResponse();
    assertionSteps.assertEndpointResponse(actual, expected);
  }

  static Stream<String> invalidActions() {
    return Stream.of(null, "", "    ", "0", "LOGIN1");
  }

  static Stream<Arguments> invalidTokens() {
    return Stream.of(
            null,
            "",
            "    ",
            "0",
            "0123456789ABCDEF0123456789ABCDE",
            "0123456789ABCDEF0123456789ABCDEF0",
            "!@#$%^")
        .flatMap(
            t ->
                Arrays.stream(EndpointRequestAction.values())
                    .map(a -> EndpointRequest.builder().token(t).action(a.name()).build()))
        .map(
            r ->
                Arguments.of(
                    Named.of("\"%1$s\" и действием %2$s".formatted(r.token(), r.action()), r)));
  }

  static Stream<Arguments> invalidApiKeys() {
    return Stream.of(null, "", "    ", "0", "LOGIN1")
        .flatMap(
            k ->
                Arrays.stream(EndpointRequestAction.values())
                    .map(a -> new ApiKeyWithAction(k, a.name())))
        .map(
            e ->
                Arguments.of(
                    Named.of("\"%1$s\" и действием %2$s".formatted(e.apiKey(), e.action()), e)));
  }

  record ApiKeyWithAction(String apiKey, String action) {}
}
