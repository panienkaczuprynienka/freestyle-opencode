package ui.tests;

import framework.base.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.HomePage;

public final class IntentionalFailureUiTest extends BaseUiTest {

  @Test(groups = {"smoke"}, description = "Validates Playwright home page title")
  public void homePageTitleContainsPlaywright() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());

    Assert.assertTrue(home.title().toLowerCase().contains("playwright"), "expected Playwright in title");
  }
}
