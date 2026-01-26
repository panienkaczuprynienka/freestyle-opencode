package framework.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

public final class AllureTestNgListener implements ITestListener {
  @Override
  public void onTestFailure(ITestResult result) {
    // Attachments are handled in BaseUiTest/BaseApiTest to keep everything in one place.
  }
}
