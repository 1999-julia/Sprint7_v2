import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourierTests {
    @BeforeClass
    public static void log() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier/login";
    }
    @Test
    @Description("Check response status code and availability body id")
    @DisplayName("Authorization registered courier")
    public void loginCourierStatusCodeAndId() {
        String json = "{\"login\": \"Julia1\", \"password\": \"Password123\", \"firstName\": \"PingPong101\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post()
                .then().statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
    }
    @Test
    @Description("Check response status code (it should be 404) and message (\"Учетная запись не найдена\")")
    @DisplayName("Authorization courier with wrong password")
    public void loginCourierWithWrongPassword() {
        String json = "{\"login\": \"Julia0\", \"password\": \"WrongPassw0rd\", \"firstName\": \"PingPong101\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post()
                .then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Check response status code (it should be 400) and message (\"Недостаточно данных для входа\")")
    @DisplayName("Authorization courier without field login")
    public void loginCourierWithouLogin(){
        String json = "{\"login\": \"\", \"password\": \"WrongPassw0rd\", \"firstName\": \"PingPong101\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post()
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @Description("Check response status code (it should be 404) and message (\"Учетная запись не найдена\")")
    @DisplayName("Authorization courier with non-existent login")
    public void loginCourierNonExistentName() {
        String json = "{\"login\": \"Julia0\", \"password\": \"Password12345\", \"firstName\": \"PingPong101\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post()
                .then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}
