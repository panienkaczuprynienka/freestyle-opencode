package framework.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public final class RetryAnalyzer implements IRetryAnalyzer {
  private int attempt = 0;

  @Override
  public boolean retry(ITestResult result) {
    int maxRetries = Integer.parseInt(System.getProperty("retryCount", "0"));
    if (maxRetries <= 0) {
      return false;
    }

    if (attempt < maxRetries) {
      attempt++;
      return true;
    }
    return false;
  }
}
