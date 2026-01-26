package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class DocsPage {
  private final Page page;

  public DocsPage(Page page) {
    this.page = page;
  }

  public void waitUntilLoaded() {
    installationHeading().waitFor();
  }

  public Locator installationHeading() {
    return page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation")).first();
  }

  public void openSearch() {
    // Docusaurus/Algolia search button in the header.
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).first().click();
    searchInput().waitFor();
  }

  public Locator searchInput() {
    return page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("Search"));
  }

  public Locator searchResultsList() {
    // In docsearch modal results are rendered as a list.
    return page.getByRole(AriaRole.LIST).first();
  }

  public void searchFor(String query) {
    openSearch();
    searchInput().fill(query);
  }
}
