package ui.tests;

import framework.base.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.DocsPage;
import ui.pages.HomePage;

public final class PlaywrightDevUiSmokeTests extends BaseUiTest {

  @Test(groups = {"smoke"}, description = "Home page loads and has Get started link")
  public void homePageLoads() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());

    Assert.assertTrue(home.title().toLowerCase().contains("playwright"), "title should contain 'playwright'");
    Assert.assertTrue(home.getStartedLink().isVisible(), "Get started link should be visible");
  }

  @Test(groups = {"smoke"}, description = "Navigation to Docs shows Installation heading")
  public void docsOpenFromTopNav() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());
    home.topNav().openDocs();

    DocsPage docs = new DocsPage(page());
    docs.waitUntilLoaded();
    Assert.assertTrue(docs.installationHeading().isVisible(), "Installation heading should be visible");
  }

  @Test(groups = {"smoke"}, description = "Docs search finds results")
  public void docsSearchFindsResults() {
    HomePage home = new HomePage(page());
    home.open(CFG.baseUrl());
    home.topNav().openDocs();

    DocsPage docs = new DocsPage(page());
    docs.waitUntilLoaded();

    docs.searchFor("locator");
    Assert.assertTrue(docs.searchResultsList().isVisible(), "Search results list should be visible");
  }
}
