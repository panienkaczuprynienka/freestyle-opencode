package ui.tests;

import framework.base.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.PlaywrightHomePage;

public final class PlaywrightDevSmokeTest extends BaseUiTest {

  @Test(groups = {"smoke"})
  public void homePageLoadsAndDocsOpen() {
    PlaywrightHomePage home = new PlaywrightHomePage(page());
    home.open(CFG.baseUrl());

    Assert.assertTrue(home.title().toLowerCase().contains("playwright"), "title should contain 'playwright'");

    home.goToDocs();
    Assert.assertTrue(home.isDocsHeadingVisible(), "docs heading should be visible");
  }
}
