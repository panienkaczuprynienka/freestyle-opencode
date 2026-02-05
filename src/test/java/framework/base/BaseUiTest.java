package framework.base;

import com.microsoft.playwright.Page;
import framework.driver.PlaywrightManager;
import framework.listeners.AllureTestNgListener;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

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
      attachScreenshot(result, page);
    }

    Path tracePath = testArtifactsDir.resolve("trace.zip");
    PlaywrightManager.stop(CFG, failed, tracePath);

    if (failed || "on".equalsIgnoreCase(CFG.traceMode())) {
      attachTraceIfExists(result, tracePath);
    }
    attachVideoIfExists(result, failed);
  }

  protected Page page() {
    return PlaywrightManager.page();
  }

  private void attachScreenshot(ITestResult result, Page page) {
    try {
      byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
      addAttachmentToTestCase(result, "screenshot", "image/png", ".png", new ByteArrayInputStream(bytes));
    } catch (RuntimeException e) {
      // Best-effort.
    }
  }

  private void attachTraceIfExists(ITestResult result, Path tracePath) {
    if (!Files.exists(tracePath)) {
      return;
    }
    try (InputStream in = Files.newInputStream(tracePath, StandardOpenOption.READ)) {
      addAttachmentToTestCase(result, "trace", "application/zip", ".zip", in);
    } catch (IOException ignored) {
    }
  }

  private void attachVideoIfExists(ITestResult result, boolean failed) {
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
        stream.filter(p -> p.toString().endsWith(".webm")).findFirst().ifPresent(path -> attachVideo(result, path));
      }
    } catch (IOException ignored) {
    }
  }

  private void attachVideo(ITestResult result, Path videoPath) {
    try (InputStream in = Files.newInputStream(videoPath, StandardOpenOption.READ)) {
      addAttachmentToTestCase(result, "video", "video/webm", ".webm", in);
    } catch (IOException ignored) {
    }
  }

  private void addAttachmentToTestCase(ITestResult result, String name, String type, String extension, InputStream in) {
    Optional<String> uuid = getTestCaseUuid(result);
    if (uuid.isEmpty()) {
      Allure.addAttachment(name, type, in, extension);
      return;
    }
    AllureLifecycle lifecycle = Allure.getLifecycle();
    String source = lifecycle.prepareAttachment(name, type, extension);
    try (InputStream input = in) {
      lifecycle.writeAttachment(source, input);
    } catch (IOException ignored) {
      return;
    }
    lifecycle.updateTestCase(uuid.get(), testResult -> testResult.getAttachments()
      .add(new Attachment().setName(name).setType(type).setSource(source)));
  }

  private Optional<String> getTestCaseUuid(ITestResult result) {
    Object value = result.getAttribute(AllureTestNgListener.TEST_CASE_UUID_ATTR);
    if (value instanceof String uuid && !uuid.isBlank()) {
      return Optional.of(uuid);
    }
    return Optional.empty();
  }

  private static String sanitize(String s) {
    return s.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}
