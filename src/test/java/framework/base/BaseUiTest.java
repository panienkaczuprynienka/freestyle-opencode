package framework.base;

import com.microsoft.playwright.Page;
import framework.driver.PlaywrightManager;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class BaseUiTest extends BaseTest {
  private Path testArtifactsDir;

  @BeforeMethod(alwaysRun = true)
  public void startBrowser(ITestResult result) {
    String testId = PlaywrightManager.defaultTestId(result.getTestClass().getName(), result.getMethod().getMethodName());
    testArtifactsDir = Path.of("target", "playwright-artifacts", sanitize(testId));
    try {
      Files.createDirectories(testArtifactsDir);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create artifacts dir: " + testArtifactsDir, e);
    }

    PlaywrightManager.start(CFG, testId, testArtifactsDir);
  }

  @AfterMethod(alwaysRun = true)
  public void stopBrowser(ITestResult result) {
    boolean failed = result.getStatus() == ITestResult.FAILURE;
    Page page = null;
    try {
      page = PlaywrightManager.page();
    } catch (RuntimeException ignored) {
    }

    if (failed && page != null) {
      attachScreenshot(page);
    }

    Path tracePath = testArtifactsDir.resolve("trace.zip");
    PlaywrightManager.stop(CFG, failed, tracePath);

    if (failed || "on".equalsIgnoreCase(CFG.traceMode())) {
      attachTraceIfExists(tracePath);
    }
    attachVideoIfExists(failed);
  }

  protected Page page() {
    return PlaywrightManager.page();
  }

  private void attachScreenshot(Page page) {
    try {
      byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
      Allure.addAttachment("screenshot", "image/png", new ByteArrayInputStream(bytes), ".png");
    } catch (RuntimeException e) {
      // Best-effort.
    }
  }

  private void attachTraceIfExists(Path tracePath) {
    if (!Files.exists(tracePath)) {
      return;
    }
    try (InputStream in = Files.newInputStream(tracePath, StandardOpenOption.READ)) {
      Allure.addAttachment("trace", "application/zip", in, ".zip");
    } catch (IOException ignored) {
    }
  }

  private void attachVideoIfExists(boolean failed) {
    if (!"on-failure".equalsIgnoreCase(CFG.videoMode())) {
      return;
    }
    if (!failed) {
      return;
    }
    Path videoDir = testArtifactsDir.resolve("video");
    if (!Files.isDirectory(videoDir)) {
      return;
    }

    try {
      try (var stream = Files.list(videoDir)) {
        stream.filter(p -> p.toString().endsWith(".webm")).findFirst().ifPresent(this::attachVideo);
      }
    } catch (IOException ignored) {
    }
  }

  private void attachVideo(Path videoPath) {
    try (InputStream in = Files.newInputStream(videoPath, StandardOpenOption.READ)) {
      Allure.addAttachment("video", "video/webm", in, ".webm");
    } catch (IOException ignored) {
    }
  }

  private static String sanitize(String s) {
    return s.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}
