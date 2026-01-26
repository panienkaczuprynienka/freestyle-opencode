package framework.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest extends BaseTest {
  @BeforeMethod(alwaysRun = true)
  public void configureRestAssured() {
    RestAssured.baseURI = CFG.apiBaseUri();
    RestAssured.filters(new AllureRestAssured());
  }
}
