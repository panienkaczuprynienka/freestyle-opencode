package framework.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public final class RetryAnalyzer implements IRetryAnalyzer {
  @Override
  public boolean retry(ITestResult result) {
    int maxRetries = Integer.parseInt(System.getProperty("retryCount", "0"));
    if (maxRetries <= 0) {
      return false;
    }

    Object attr = result.getAttribute("retryAttempt");
    int attempt = attr instanceof Integer ? (Integer) attr : 0;
    if (attempt < maxRetries) {
      result.setAttribute("retryAttempt", attempt + 1);
      return true;
    }

    return false;
  }
}
