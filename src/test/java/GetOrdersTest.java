import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetOrdersTest {
    public String accessToken;

    @Before
    public void setUp() {
        baseURI = Endpoints.BASE_URL;
    }

    @Test
    @DisplayName("Получение заказов с авторизацией")
    @Description("API - /api/orders")
    public void getAllOrdersAuthorizedTest() {
        accessToken = UserCreate.createNewRandomUser();
        given()
                .header("Authorization", "Bearer" + accessToken)
                .get(Endpoints.GET_USER_ORDERS)
                .then().log().all().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    @Description("API - /api/orders/all")
    public void getAllOrdersUnauthorizedTest() {
        given()
                .get(Endpoints.GET_USER_ORDERS)
                .then().log().all().statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

}
