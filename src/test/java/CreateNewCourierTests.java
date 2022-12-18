import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import courier.*;

public class CreateNewCourierTests {
    private final CourierGenerator courierGenerator = new CourierGenerator();
    private CourierClient courierClient;
    private Courier courier;
    private CourierAssertions courierAssertions;
    int idCourier;

    @Before
    @Step("Precondition step for creating courier tests")
    public void setUp() {
        courierClient = new CourierClient();
        courier = courierGenerator.getCourierRandom();
        courierAssertions = new CourierAssertions();
    }

    @Test
    @DisplayName("Create new courier")
    @Description("Test for successfully creation with status code and message")
    public void createNewCourier() {
        ValidatableResponse responseCreateCourier = courierClient.createCourier(courier);
        courierAssertions.creatingCourierSuccessfully(responseCreateCourier);
        Identity identity = Identity.from(courier);
        ValidatableResponse responseLoginCourier = courierClient.loginCourier(identity);
        idCourier = responseLoginCourier.extract().path("id");
    }

    @Test
    @DisplayName("Create courier without login field")
    @Description("Test to try create new courier without login field. Check status code and message")
    public void courierCanNotBeCreatedWithoutLoginField() {
        courier.setLogin(null);
        ValidatableResponse responseNullLogin = courierClient.createCourier(courier);
        courierAssertions.creationCourierFailedField(responseNullLogin);
    }

    @Test
    @DisplayName("Create courier without password field")
    @Description("Test to try create new courier without password field. Check status code and message")
    public void courierCanNotBeCreatedWithoutPasswordField() {
        courier.setPassword(null);
        ValidatableResponse responseNullPassword = courierClient.createCourier(courier);
        courierAssertions.creationCourierFailedField(responseNullPassword);
    }

    @Test
    @DisplayName("Create courier without login and password fields")
    @Description("Test to try create new courier without login and password fields. Check status code and message")
    public void courierCanNotBeCreatedWithoutLoginAndPasswordFields() {
        courier.setLogin(null);
        courier.setPassword(null);
        ValidatableResponse responseNullFields = courierClient.createCourier(courier);
        courierAssertions.creationCourierFailedField(responseNullFields);
    }

    @Test
    @DisplayName("Create courier with existing data")
    @Description("Test to try create courier with the same/existing data. Check status code and message.")
    public void courierCanNotBeCreatedWithTheSameData() {
        courierClient.createCourier(courier);
        ValidatableResponse responseCreateCourier = courierClient.createCourier(courier);
        courierAssertions.creationCourierTheSameData(responseCreateCourier);
    }

    @After
    @Step("Delete test courier")
    public void deleteCourier() {
        if (idCourier != 0) {
            courierClient.deleteCourier(idCourier);
        }
    }
}


