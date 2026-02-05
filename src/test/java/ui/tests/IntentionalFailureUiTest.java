package ui.tests;

import framework.base.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.HomePage;

public final class IntentionalFailureUiTest extends BaseUiTest {

  @Test(groups = {"smoke"}, description = "Validates Playwright home page loads")
  public void homePageLoads() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());

    home.getStartedLink().waitFor();
    Assert.assertTrue(home.getStartedLink().isVisible(), "expected Get started link on home page");
  }
}
