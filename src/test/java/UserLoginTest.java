import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import pojo.UserData;

public class UserLoginTest {
    private String accessToken;

    @Before
    public void setUp() {
        baseURI = Endpoints.BASE_URL;
        UserCreate.createBaseUser();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("API - /api/auth/login")
    public void loginAsExistedUserTest() {
        UserData userData = new UserData();
        userData.setEmail("alexey@yandex.ru");
        userData.setPassword("123456");
        Response response =
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.LOGIN);
                response.then().log().all().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("alexey@yandex.ru"))
                .body("user.name", equalTo("alexeytestbaseuser"))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
                accessToken = response.then().extract().path("accessToken");

    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("API - /api/auth/login")
    public void loginWithInvalidEmailTest() {
        UserData userData = new UserData();
        userData.setEmail("test-data@");
        userData.setPassword("password");

        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.LOGIN)
                .then().log().all().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным password")
    @Description("API - /api/auth/login")
    public void loginWithInvalidPasswordTest() {
        UserData userData = new UserData();
        userData.setEmail("test-data@yandex.ru");
        userData.setPassword("passwordd");

        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.LOGIN)
                .then().log().all().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    public void deleteTestData() {
        UserCreate.deleteUser(accessToken);
    }
}
