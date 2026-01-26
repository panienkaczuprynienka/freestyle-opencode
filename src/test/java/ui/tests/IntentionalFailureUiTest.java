package ui.tests;

import framework.base.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.HomePage;

public final class IntentionalFailureUiTest extends BaseUiTest {

  @Test(groups = {"smoke", "no-retry"}, description = "INTENTIONAL FAIL: verifies screenshot attachment in Allure")
  public void intentionalFailureToSeeScreenshotInAllure() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());

    // Intentional failure: Playwright.dev title does not contain this word.
    Assert.assertTrue(home.title().toLowerCase().contains("selenium"), "intentional failure");
  }
}
