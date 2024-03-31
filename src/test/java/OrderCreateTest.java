import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrderCreateTest {
    private String accessToken;

    @Before
    public void setUp() {
        baseURI = Endpoints.BASE_URL;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("API - /api/orders")
    public void createNewOrderAuthorizedTest() {
        accessToken = UserCreate.createNewRandomUser();
        String json = "{\n" + "\"ingredients\": [\n" + "\"61c0c5a71d1f82001bdaaa73\",\n" + "\"61c0c5a71d1f82001bdaaa70\",\n" + "\"61c0c5a71d1f82001bdaaa77\",\n" + "\"61c0c5a71d1f82001bdaaa7a\",\n" + "\"61c0c5a71d1f82001bdaaa6c\"\n" + "]\n" + "}";
        given()
                .header("Authorization", "Bearer" + accessToken)
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(Endpoints.CREATE_NEW_ORDER)
                .then().log().all().statusCode(200)
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("API - /api/orders")
    public void createNewOrderUnauthorizedTest() {
        String json = "{\n" + "\"ingredients\": [\n" + "\"61c0c5a71d1f82001bdaaa73\",\n" + "\"61c0c5a71d1f82001bdaaa70\",\n" + "\"61c0c5a71d1f82001bdaaa77\",\n" + "\"61c0c5a71d1f82001bdaaa7a\",\n" + "\"61c0c5a71d1f82001bdaaa6c\"\n" + "]\n" + "}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(Endpoints.CREATE_NEW_ORDER)
                .then().log().all().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("API - /api/orders")
    public void createNewOrderWithNoIngredientsTest() {
        accessToken = UserCreate.createNewRandomUser();
        String json = "";
        given()
                .header("Authorization", "Bearer" + accessToken)
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(Endpoints.CREATE_NEW_ORDER)
                .then().log().all().statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиентов")
    @Description("API - /api/orders")
    public void createNewOrderWithInvalidIngredientHashTest() {
        String json = "{\n" + "\"ingredients\": [\n" + "\"61c0c5a71d1f82001bdaa73\",\n" + "\"61c0c5a71d1f82001bdaaa70\",\n" + "\"61c0c5a71d1f82001bdaaa77\",\n" + "\"61c0c5a71d1f82001bdaaa7a\",\n" + "\"61c0c5a71d1f82001bdaaa6c\"\n" + "]\n" + "}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(Endpoints.CREATE_NEW_ORDER)
                .then().log().all().statusCode(500);
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    public void deleteTestData() {
        UserCreate.deleteUser(accessToken);
    }

}
