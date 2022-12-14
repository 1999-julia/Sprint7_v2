import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.response.Response;
import org.junit.Before;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
public class CreateNewCourierTests {

    @BeforeClass
    public static void log() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }
    @Step("Create new courier (Send POST request)")
    public Response sendPostRequestCreateNewCourier(String json){
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post();
        return response;
    }
    @Step("Check status code and body")
    public void checkStatusCode400(Response response){
        response.then().assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Step("Check status code and body")
    public void checkStatusCode409(Response response){
        response.then().assertThat().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Step("Check status code and body")
    public void checkStatusCode201(Response response){
        response.then().assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
    }
    @Test
    @Description("Always create original login for new courier. Check response status code and availability body \"ok: true\"")
    @DisplayName("Create new courier")
    public void createNewCourier() {
        String json = "{\"login\": \"Julia575\", \"password\": \"P@ssw0rd123\", \"firstName\": \"Sparrow\"}";
        Response response = sendPostRequestCreateNewCourier(json);
        checkStatusCode201(response);
    }

    @Test
    @Description("Test to try create courier with the same/existing data. Check status code and message.")
    @DisplayName("Create new courier with exiting login")
    public void createNewCourierWithTheSameName() {
        String json = "{\"login\": \"Julia1\", \"password\": \"Password123\", \"firstName\": \"PingPong101\"}";
        Response response = sendPostRequestCreateNewCourier(json);
        checkStatusCode409(response);
    }
    @Test
    @DisplayName("Create courier without login field")
    @Description("Test to try create new courier without login field. Check status code and message")
    public void createNewCourierWithoutLogin() {
        String json = "{\"login\": \"Julia69\", \"password\": \"\", \"firstName\": \"PingPong101\"}";
        Response response = sendPostRequestCreateNewCourier(json);
        checkStatusCode400(response);
    }
    @Test
    @Description("Test to try create new courier without password field. \"Недостаточно данных для создания учетной записи\")")
    @DisplayName("Create new courier without field password")
    public void createNewCourierWithoutPassword() {
        String json = "{\"login\": \"Julia66\", \"password\": \"\", \"firstName\": \"PingPong101\"}";
        Response response = sendPostRequestCreateNewCourier(json);
        checkStatusCode400(response);
    }
    @Test
    @Description("Always create original login (status code should be 201)")
    @DisplayName("Create new courier without field firstName")
    public void createNewCourierWithoutFirstName() {
        String json = "{\"login\": \"julia8858\", \"password\": \"P@ssw0rd123\", \"firstName\": \"\"}";
        Response response = sendPostRequestCreateNewCourier(json);
        checkStatusCode201(response);
    }
}

