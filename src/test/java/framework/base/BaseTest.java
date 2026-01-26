package framework.base;

import framework.config.TestConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import framework.retry.RetryTransformer;

@Listeners({RetryTransformer.class})
public abstract class BaseTest {
  protected static TestConfig CFG;

  @BeforeClass(alwaysRun = true)
  public void loadConfigOnce() {
    ensureConfigLoaded();
  }

  protected static synchronized void ensureConfigLoaded() {
    if (CFG == null) {
      CFG = TestConfig.load();
    }
  }
}
