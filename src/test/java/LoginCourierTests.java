import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import courier.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginCourierTests {
    private final CourierGenerator courierGenerator = new CourierGenerator();
    private Identity identity;
    private CourierClient courierClient;
    private Courier courier;
    CourierAssertions courierAssertions;
    int idCourier;

    @Before
    @Step("Precondition for login tests with creation courier")
    public void setUp() {
        courierClient = new CourierClient();
        courier = courierGenerator.getCourierRandom();
        courierClient.createCourier(courier);
        identity = Identity.from(courier);
        courierAssertions = new CourierAssertions();
    }

    @Test
    @Description("Check response status code and availability body id")
    @DisplayName("Authorization registered courier")
    public void courierCanSuccessfullyLogin() {
        ValidatableResponse responseLoginCourier = courierClient.loginCourier(identity);
        courierAssertions.LoginCourierSuccessfully(responseLoginCourier);
        idCourier = responseLoginCourier.extract().path("id");
    }

    @Test
    @Description("Check response status code (it should be 400) and message (\"Недостаточно данных для входа\")")
    @DisplayName("Authorization courier without field login")
    public void courierLoginUnsuccessfullyWithoutLogin() {
        Identity identityWithoutLogin = new Identity("", courier.getPassword()); // c null тесты виснут
        ValidatableResponse responseLoginErrorMessage = courierClient.loginCourier(identityWithoutLogin).statusCode(400);
        responseLoginErrorMessage.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login courier without password field")
    @Description("Try to login courier without password field. Check status code and message.")
    public void courierLoginUnsuccessfullyWithoutPassword() {
        Identity identityWithoutLogin = new Identity(courier.getLogin(), "");
        ValidatableResponse responsePasswordErrorMessage = courierClient.loginCourier(identityWithoutLogin).statusCode(400);
        responsePasswordErrorMessage.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Check response status code (it should be 404) and message (\"Учетная запись не найдена\")")
    @DisplayName("Authorization courier with wrong password")
    public void loginCourierWithoutLogin() {
        Identity identityWithoutLoginAndPassword = new Identity("", "");
        ValidatableResponse responseWithoutLoginAndPasswordMessage = courierClient.loginCourier(identityWithoutLoginAndPassword).statusCode(400);
        responseWithoutLoginAndPasswordMessage.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Check response status code (it should be 404) and message (\"Учетная запись не найдена\")")
    @DisplayName("Authorization courier with non-existent login")
    public void loginCourierNonExistentName() {
        Identity identityWithNotExistingLogin = new Identity(RandomStringUtils.randomAlphanumeric(6), courier.getPassword());
        ValidatableResponse responseWithWithNotExistingLoginMessage = courierClient.loginCourier(identityWithNotExistingLogin).statusCode(404);
        responseWithWithNotExistingLoginMessage.assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    @Step("Delete test courier")
    public void deleteCourier() {
        if (idCourier != 0) {
            courierClient.deleteCourier(idCourier);
        }
    }
}
