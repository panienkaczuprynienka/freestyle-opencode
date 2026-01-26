package api.tests;

import framework.base.BaseApiTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public final class HttpBinSmokeTest extends BaseApiTest {
  @Test(groups = {"smoke"})
  public void getReturns200() {
    RestAssured
      .given()
      .when()
      .get("/get")
      .then()
      .statusCode(200)
      .body("url", equalTo(CFG.apiBaseUri() + "/get"));
  }
}
