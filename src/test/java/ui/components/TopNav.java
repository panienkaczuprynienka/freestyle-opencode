package ui.components;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class TopNav {
  private final Page page;

  public TopNav(Page page) {
    this.page = page;
  }

  public void openDocs() {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Docs")).first().click();
    page.waitForURL("**/docs/**");
  }

  public void openAPI() {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("API")).first().click();
    page.waitForURL("**/docs/api/**");
  }
}
