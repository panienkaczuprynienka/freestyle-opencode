package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import ui.components.TopNav;

public final class HomePage {
  private final Page page;

  public HomePage(Page page) {
    this.page = page;
  }

  public void open(String baseUrl) {
    page.navigate(baseUrl);
  }

  public String title() {
    return page.title();
  }

  public TopNav topNav() {
    return new TopNav(page);
  }

  public Locator getStartedLink() {
    return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started"));
  }
}
