package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class TestConfig {
  private final Properties props;

  private TestConfig(Properties props) {
    this.props = props;
  }

  public static TestConfig load() {
    String env = System.getProperty("env", "stage");
    String path = "config/" + env + ".properties";

    Properties p = new Properties();
    try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream(path)) {
      if (in == null) {
        throw new IllegalStateException("Missing config file on classpath: " + path);
      }
      p.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load config: " + path, e);
    }

    applyOverride(p, "baseUrl");
    applyOverride(p, "apiBaseUri");
    applyOverride(p, "browser");
    applyOverride(p, "headless");
    applyOverride(p, "timeoutMs");
    applyOverride(p, "navigationTimeoutMs");
    applyOverride(p, "trace");
    applyOverride(p, "video");

    return new TestConfig(p);
  }

  private static void applyOverride(Properties p, String key) {
    String v = System.getProperty(key);
    if (v != null && !v.isBlank()) {
      p.setProperty(key, v);
    }
  }

  public String env() {
    return System.getProperty("env", "stage");
  }

  public String baseUrl() {
    return require("baseUrl");
  }

  public String apiBaseUri() {
    return require("apiBaseUri");
  }

  public String browser() {
    return props.getProperty("browser", "chromium");
  }

  public boolean headless() {
    return Boolean.parseBoolean(props.getProperty("headless", "true"));
  }

  public int timeoutMs() {
    return Integer.parseInt(props.getProperty("timeoutMs", "30000"));
  }

  public int navigationTimeoutMs() {
    return Integer.parseInt(props.getProperty("navigationTimeoutMs", "45000"));
  }

  public String traceMode() {
    return props.getProperty("trace", "on-failure");
  }

  public String videoMode() {
    return props.getProperty("video", "off");
  }

  private String require(String key) {
    String v = props.getProperty(key);
    if (v == null || v.isBlank()) {
      throw new IllegalStateException("Missing required config key: " + key);
    }
    return Objects.requireNonNull(v);
  }
}
