package ru.tonyp.extensions;

import org.jspecify.annotations.NullMarked;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import ru.tonyp.dto.EndpointRequest;
import ru.tonyp.dto.EndpointRequestAction;
import ru.tonyp.extensions.injector.InjectorService;
import ru.tonyp.steps.ActionSteps;
import ru.tonyp.utils.FakerUtils;

@NullMarked
public class EndpointTestExecutionListener implements TestExecutionListener {

  private final FakerUtils fakerUtils = InjectorService.getInstance(FakerUtils.class);
  private final ActionSteps actionSteps = InjectorService.getInstance(ActionSteps.class);

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    for (String token : fakerUtils.getAllGeneratedTokens()) {
      try {
        actionSteps.sendEndpointRequest(
            EndpointRequest.builder()
                .token(token)
                .action(EndpointRequestAction.LOGOUT.name())
                .build());
      } catch (Throwable ignored) {
      }
    }
  }
}
