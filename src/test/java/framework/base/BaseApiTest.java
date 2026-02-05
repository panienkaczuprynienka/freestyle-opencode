package framework.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest extends BaseTest {
  @BeforeMethod(alwaysRun = true)
  public void validateApiConfig() {
    ensureConfigLoaded();
    String apiBaseUri = CFG.apiBaseUri();
    if (apiBaseUri == null || apiBaseUri.isBlank()) {
      throw new IllegalStateException("apiBaseUri must be set for API tests");
    }
  }

  protected RequestSpecification api() {
    return RestAssured.given()
      .baseUri(CFG.apiBaseUri())
      .filter(new AllureRestAssured());
  }
}
