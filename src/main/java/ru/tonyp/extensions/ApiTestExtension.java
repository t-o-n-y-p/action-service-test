package ru.tonyp.extensions;

import io.qameta.allure.Step;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.opentest4j.TestAbortedException;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointRequestAction;
import ru.tonyp.extensions.annotations.ActionErrorToken;
import ru.tonyp.extensions.annotations.ActionSuccessToken;
import ru.tonyp.extensions.annotations.RevokedToken;
import ru.tonyp.extensions.injector.InjectorService;
import ru.tonyp.steps.ActionSteps;
import ru.tonyp.steps.PreparationSteps;

@NullMarked
public class ApiTestExtension
    implements TestInstancePostProcessor, ParameterResolver, TestExecutionExceptionHandler {

  private final ActionSteps actionSteps = InjectorService.getInstance(ActionSteps.class);
  private final PreparationSteps preparationSteps =
      InjectorService.getInstance(PreparationSteps.class);

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
    InjectorService.injectMembers(testInstance);
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return String.class.isAssignableFrom(parameterContext.getParameter().getType())
        && (parameterContext.isAnnotated(ActionSuccessToken.class)
            || parameterContext.isAnnotated(ActionErrorToken.class)
            || parameterContext.isAnnotated(RevokedToken.class));
  }

  @Override
  @Step("Подготовить токен, удовлетворяющий условиям")
  public @Nullable Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    EndpointRequest request =
        parameterContext.isAnnotated(ActionErrorToken.class)
            ? preparationSteps.prepareRequestWithRandomActionErrorToken()
            : preparationSteps.prepareRequestWithRandomToken(EndpointRequestAction.LOGIN);
    actionSteps.sendEndpointRequest(request);
    if (parameterContext.isAnnotated(RevokedToken.class)) {
      actionSteps.sendEndpointRequest(
          EndpointRequest.builder()
              .token(request.token())
              .action(EndpointRequestAction.LOGOUT.name())
              .build());
    }
    return request.token();
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    if (throwable instanceof ParameterResolutionException) {
      throw new TestAbortedException("Тест пропущен, так как подготовка не прошла успешно");
    }
    throw throwable;
  }
}
