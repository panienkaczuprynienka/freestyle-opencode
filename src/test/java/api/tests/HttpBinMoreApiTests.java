package api.tests;

import framework.base.BaseApiTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public final class HttpBinMoreApiTests extends BaseApiTest {

  @Test(groups = {"smoke"}, description = "Request headers are echoed back")
  public void headersAreReturned() {
    RestAssured
      .given()
      .header("X-Demo", "hello")
      .when()
      .get("/headers")
      .then()
      .statusCode(200)
      .body("headers", notNullValue())
      .body("headers.X-Demo", equalTo("hello"));
  }

  @Test(groups = {"smoke"}, description = "Status endpoint returns expected status")
  public void statusEndpointWorks() {
    RestAssured
      .given()
      .when()
      .get("/status/204")
      .then()
      .statusCode(204);
  }
}
