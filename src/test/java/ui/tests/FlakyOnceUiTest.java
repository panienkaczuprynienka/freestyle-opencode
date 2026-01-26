package ui.tests;

import framework.base.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.HomePage;

import java.util.concurrent.atomic.AtomicInteger;

public final class FlakyOnceUiTest extends BaseUiTest {
  private static final AtomicInteger ATTEMPTS = new AtomicInteger(0);

  @Test(groups = {"smoke"}, description = "FLAKY DEMO: fails once, passes on retry")
  public void flakyOnce() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());

    int attempt = ATTEMPTS.incrementAndGet();
    if (attempt == 1) {
      Assert.fail("intentional first attempt failure to validate Allure retries");
    }

    Assert.assertTrue(home.title().toLowerCase().contains("playwright"), "should pass on retry");
  }
}
