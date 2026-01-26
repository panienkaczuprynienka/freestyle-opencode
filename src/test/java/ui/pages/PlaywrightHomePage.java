package ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class PlaywrightHomePage {
  private final Page page;

  public PlaywrightHomePage(Page page) {
    this.page = page;
  }

  public void open(String baseUrl) {
    page.navigate(baseUrl);
  }

  public String title() {
    return page.title();
  }

  public void goToDocs() {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Docs")).first().click();
    page.waitForURL("**/docs/**");
  }

  public boolean isDocsHeadingVisible() {
    var heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation")).first();
    heading.waitFor();
    return heading.isVisible();
  }
}
