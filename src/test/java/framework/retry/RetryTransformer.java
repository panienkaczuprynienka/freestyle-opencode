package framework.retry;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class RetryTransformer implements IAnnotationTransformer {
  @Override
  public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    int maxRetries = Integer.parseInt(System.getProperty("retryCount", "0"));
    if (maxRetries <= 0) {
      return;
    }

    String[] groups = annotation.getGroups();
    if (groups != null && Arrays.asList(groups).contains("no-retry")) {
      return;
    }

    annotation.setRetryAnalyzer(RetryAnalyzer.class);
  }
}
