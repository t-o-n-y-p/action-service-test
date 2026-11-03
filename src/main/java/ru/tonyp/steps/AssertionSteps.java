package ru.tonyp.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Singleton;
import io.qameta.allure.Step;
import ru.tonyp.dto.EndpointResponse;

@Singleton
public class AssertionSteps {

  @Step("Проверить ответ на запрос выполнения действия")
  public void assertEndpointResponse(EndpointResponse actual, EndpointResponse expected) {
    assertThat(actual)
        .usingRecursiveComparison()
        .as("Ответ сервера не соответствует ожидаемому")
        .isEqualTo(expected);
  }
}
