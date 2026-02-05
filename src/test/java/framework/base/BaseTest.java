package framework.base;

import framework.config.TestConfig;
import framework.listeners.AllureTestNgListener;
import framework.retry.RetryTransformer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners({RetryTransformer.class, AllureTestNgListener.class})
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
