import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import order.Order;
import order.OrderClient;

import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class CreateNewOrderTests {
    private OrderClient orderClient;
    private final String[] color;
    private int track;

    public CreateNewOrderTests(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{}}
        };
    }

    @Test
    @Description("Create order with different parameters of colour.")
    @DisplayName("Create order with different colour")
    public void createOrderWithDifferentDataColor() {
        Order order = new Order(color);
        ValidatableResponse responseCreateOrder = orderClient.createNewOrder(order);
        track = responseCreateOrder.extract().path("track");
        responseCreateOrder.assertThat()
                .statusCode(201)
                .body("track", is(notNullValue()));
    }

    @After
    @Step("Cancel test order")
    public void deleteTestOrder() {
        orderClient.cancelOrder(track);
    }
}

