package framework.driver;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import framework.config.TestConfig;

import java.nio.file.Path;
import java.time.Instant;

public final class PlaywrightManager {
  private static final ThreadLocal<Playwright> TL_PLAYWRIGHT = new ThreadLocal<>();
  private static final ThreadLocal<Browser> TL_BROWSER = new ThreadLocal<>();
  private static final ThreadLocal<BrowserContext> TL_CONTEXT = new ThreadLocal<>();
  private static final ThreadLocal<Page> TL_PAGE = new ThreadLocal<>();

  private PlaywrightManager() {
  }

  public static void start(TestConfig cfg, String testId, Path artifactsDir) {
    if (!"chromium".equalsIgnoreCase(cfg.browser())) {
      throw new IllegalArgumentException("Only chromium is supported right now. Got: " + cfg.browser());
    }

    Playwright pw = Playwright.create();
    Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(cfg.headless()));

    Browser.NewContextOptions ctxOptions = new Browser.NewContextOptions();
    if ("on-failure".equalsIgnoreCase(cfg.videoMode())) {
      ctxOptions.setRecordVideoDir(artifactsDir.resolve("video"));
    }

    BrowserContext context = browser.newContext(ctxOptions);
    context.setDefaultTimeout(cfg.timeoutMs());
    context.setDefaultNavigationTimeout(cfg.navigationTimeoutMs());

    Page page = context.newPage();

    TL_PLAYWRIGHT.set(pw);
    TL_BROWSER.set(browser);
    TL_CONTEXT.set(context);
    TL_PAGE.set(page);

    if (!"off".equalsIgnoreCase(cfg.traceMode())) {
      context.tracing().start(new Tracing.StartOptions()
        .setScreenshots(true)
        .setSnapshots(true)
        .setSources(true));
    }
  }

  public static Page page() {
    Page p = TL_PAGE.get();
    if (p == null) {
      throw new IllegalStateException("Playwright Page not initialized for current thread");
    }
    return p;
  }

  public static BrowserContext context() {
    BrowserContext c = TL_CONTEXT.get();
    if (c == null) {
      throw new IllegalStateException("Playwright Context not initialized for current thread");
    }
    return c;
  }

  public static void stop(TestConfig cfg, boolean failed, Path tracePath) {
    BrowserContext context = TL_CONTEXT.get();
    try {
      if (context != null && !"off".equalsIgnoreCase(cfg.traceMode())) {
        if (failed || "on".equalsIgnoreCase(cfg.traceMode())) {
          context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        } else {
          context.tracing().stop();
        }
      }
    } catch (RuntimeException ignored) {
      // Best-effort for diagnostics; do not hide original test failure.
    }

    Page page = TL_PAGE.get();
    if (page != null) {
      try { page.close(); } catch (RuntimeException ignored) {}
    }

    if (context != null) {
      try { context.close(); } catch (RuntimeException ignored) {}
    }

    Browser browser = TL_BROWSER.get();
    if (browser != null) {
      try { browser.close(); } catch (RuntimeException ignored) {}
    }

    Playwright pw = TL_PLAYWRIGHT.get();
    if (pw != null) {
      try { pw.close(); } catch (RuntimeException ignored) {}
    }

    TL_PAGE.remove();
    TL_CONTEXT.remove();
    TL_BROWSER.remove();
    TL_PLAYWRIGHT.remove();
  }

  public static String defaultTestId(String className, String methodName) {
    return className + "." + methodName + "-" + Instant.now().toEpochMilli();
  }
}
