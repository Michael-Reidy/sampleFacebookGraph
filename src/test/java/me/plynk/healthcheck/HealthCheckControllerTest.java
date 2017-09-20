package me.plynk.healthcheck;

import org.junit.Test;

import me.plynk.healthcheck.HealthCheckController;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HealthCheckControllerTest {

  private HealthCheckController healthCheckController = new HealthCheckController();

  @Test
  public void healthCheckShouldReturnUp() throws Exception {

    String result = healthCheckController.healthCheck();
    assertThat(result, is("up"));
  }

}