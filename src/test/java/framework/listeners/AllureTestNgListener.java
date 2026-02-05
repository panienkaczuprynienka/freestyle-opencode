package framework.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Optional;

public final class AllureTestNgListener implements ITestListener {
  public static final String TEST_CASE_UUID_ATTR = "allureTestCaseUuid";

  @Override
  public void onTestStart(ITestResult result) {
    Optional<String> uuid = Allure.getLifecycle().getCurrentTestCase();
    uuid.ifPresent(value -> result.setAttribute(TEST_CASE_UUID_ATTR, value));
  }
}
