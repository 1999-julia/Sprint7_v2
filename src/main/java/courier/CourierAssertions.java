package courier;

import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CourierAssertions {
    public void creatingCourierSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(201)
                .body("ok", is(true));
    }
    public void LoginCourierSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("id", greaterThan(0));
    }
    public void creationCourierFailedField(ValidatableResponse response) {
        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    public void creationCourierTheSameData(ValidatableResponse response) {
        response.assertThat()
                .statusCode(409)
                .body("message" , equalTo("Этот логин уже используется. Попробуйте другой."));
    }


}

