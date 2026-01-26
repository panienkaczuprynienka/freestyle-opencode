package framework.base;

import framework.config.TestConfig;
import org.testng.annotations.BeforeClass;

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
